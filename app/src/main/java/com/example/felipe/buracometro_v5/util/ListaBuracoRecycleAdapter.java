package com.example.felipe.buracometro_v5.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.felipe.buracometro_v5.R;
import com.example.felipe.buracometro_v5.listeners.OnLoadMoreListener;
import com.example.felipe.buracometro_v5.listeners.RecyclerViewClickListener;
import com.example.felipe.buracometro_v5.modelo.Buraco;

import java.util.List;

public class ListaBuracoRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private RecyclerViewClickListener itemListener;

    private List<Buraco> buracosItems;
    private boolean tampados;
    boolean comOcorrencias = false;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;


    public ListaBuracoRecycleAdapter(Context context, List<Buraco> buracosItems, RecyclerView mRecyclerView, RecyclerViewClickListener itemListener) {

        this.itemListener = itemListener;
        this.context = context;
        this.buracosItems = buracosItems;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                if(dy > 1) //check for scroll down
                {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    public ListaBuracoRecycleAdapter(Context context, List<Buraco> buracosItems, boolean tampados, RecyclerView mRecyclerView, RecyclerViewClickListener itemListener) {

        this.itemListener = itemListener;
        this.context = context;
        this.buracosItems = buracosItems;
        this.tampados = tampados;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public ListaBuracoRecycleAdapter(Context context, List<Buraco> buracosItems, RecyclerView mRecyclerView, RecyclerViewClickListener itemListener, boolean comOcorrencias) {

        this.itemListener = itemListener;
        this.context = context;
        this.buracosItems = buracosItems;
        this.tampados = tampados;
        this.comOcorrencias = comOcorrencias;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return buracosItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public Buraco getItem(int position){
        return buracosItems.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conteudo_listview_buracos, viewGroup, false);
            return new CustomViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_progress_bar, viewGroup, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {

            CustomViewHolder customViewHolder = (CustomViewHolder) holder;

            customViewHolder.textoEnderecoCriticos.setText(buracosItems.get(i).getRua());
            customViewHolder.textoCidadeCriticos.setText(buracosItems.get(i).getCidade());
            customViewHolder.textoEstadoCriticos.setText(buracosItems.get(i).getEstado());
            customViewHolder.textoOcorrencias.setText(Integer.toString(buracosItems.get(i).getQtdOcorrencia()));

            customViewHolder.botaoMapearCriticos.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    itemListener.recyclerViewListClicked(v,i);
                }
            });

            if(tampados){
                customViewHolder.textoDaData.setText("Data Tampado: ");
                customViewHolder.textoDataCriticos.setText(buracosItems.get(i).getDataTampado());

            }else{
                customViewHolder.textoDataCriticos.setText(buracosItems.get(i).getData_Registro());
            }

            if(comOcorrencias){
                customViewHolder.textoOcorrencias.setVisibility(View.VISIBLE);
                customViewHolder.textoDoRegistros.setVisibility(View.VISIBLE);
            }


        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return (null != buracosItems ? buracosItems.size() : 0);
    }

    public void setLoaded() {
        isLoading = false;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textoEnderecoCriticos;
        TextView textoCidadeCriticos;
        TextView textoEstadoCriticos;
        TextView textoDataCriticos;
        TextView textoOcorrencias;
        TextView textoDoRegistros;
        TextView textoDaData;
        ImageButton botaoMapearCriticos;

        public CustomViewHolder(View view) {
            super(view);

            this.botaoMapearCriticos = (ImageButton)view.findViewById(R.id.btnOpcoesCriticos);

            this.textoEnderecoCriticos = (TextView)view.findViewById(R.id.textoEnderecoCriticos);
            this.textoCidadeCriticos = (TextView)view.findViewById(R.id.textoCidadeCriticos);
            this.textoEstadoCriticos = (TextView)view.findViewById(R.id.textoEstadoCriticos);
            this.textoDataCriticos = (TextView)view.findViewById(R.id.textoDataCriticos);
            this.textoOcorrencias = (TextView)view.findViewById(R.id.textoOcorrencias);
            this.textoDoRegistros = (TextView)view.findViewById(R.id.Ocorrencias);
            this.textoDaData = (TextView)view.findViewById(R.id.Data);

        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


}