package com.volnei.soccernews.ui.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.volnei.soccernews.MainActivity;
import com.volnei.soccernews.databinding.FragmentNewsBinding;
import com.volnei.soccernews.ui.adapter.NewsAdapter;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        newsViewModel.getNews().observe(getViewLifecycleOwner(), news ->
                binding.rvNews.setAdapter(new NewsAdapter(news, updatedNews -> {
                    MainActivity activity = (MainActivity) getActivity();
                    AsyncTask.execute(() -> {
                        if (activity != null) {
                            activity.getDb().newsDao().save(updatedNews);
                        }
                    });
                })));

        newsViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case DOING: // TODO Iniciar SwiperRefreshLayout (loading)
                    break;
                case DONE: // TODO Finalizar SwiperRefreshLayout (loading)
                    break;
                case ERROR: // TODO Finalizar SwiperRefreshLayout (loading); Mostrar erro genérico
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}