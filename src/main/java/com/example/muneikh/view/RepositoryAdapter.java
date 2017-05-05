/**
 * Copyright 2016 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.muneikh.view;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muneikh.R;
import com.example.muneikh.model.RepositoryModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryAdapterViewHolder> {

    private List<RepositoryModel.Repository> repositories;

    public RepositoryAdapter() {
        this.repositories = Collections.emptyList();
    }

    @Override
    public RepositoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repository, parent, false);
        return new RepositoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryAdapterViewHolder holder, int position) {
        holder.bindRepository(repositories.get(position));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void setRepositories(List<RepositoryModel.Repository> repositories) {
        this.repositories = repositories;
        notifyDataSetChanged();
    }

    public static class RepositoryAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.login)
        TextView ownerLoginName;

        View view;

        public RepositoryAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        void bindRepository(RepositoryModel.Repository repository) {
            name.setText(repository.getName());
            description.setText(repository.getDescription());
            ownerLoginName.setText(repository.getRepositoryOwner().getLogin());

            if (repository.isForked()) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorGreen));
            } else {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorWhite));
            }

            view.setOnLongClickListener(v -> {
                setupAlertDialog(repository);
                return true;
            });
        }

        private void setupAlertDialog(RepositoryModel.Repository repository) {
            String ownerUrl = repository.getRepositoryOwner().getOwnerUrl();
            String repositoryUrl = repository.getRepositoryUrl();

            SpannableString ss = new SpannableString(ownerUrl + "\n\n" + repositoryUrl);
            Linkify.addLinks(ss, Linkify.WEB_URLS);

            AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                    .setCancelable(true)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(R.string.cancel, null)
                    .setMessage(ss)
                    .create();
            dialog.show();
            ((TextView) dialog.findViewById(android.R.id.message))
                    .setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}