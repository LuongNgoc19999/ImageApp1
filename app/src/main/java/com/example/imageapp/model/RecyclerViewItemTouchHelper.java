package com.example.imageapp.model;

import android.graphics.Canvas;
import android.net.sip.SipSession;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageapp.adapter.AdapterLocalImage;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {
        ItemTouchHelperListener itemTouchHelperListener;
    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        itemTouchHelperListener = listener;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(itemTouchHelperListener !=null){
            itemTouchHelperListener.onSwipe(viewHolder);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable @org.jetbrains.annotations.Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View foregroundView = ((AdapterLocalImage.ViewHolder) viewHolder).binding.layoutForeground;
            getDefaultUIUtil().onSelected(foregroundView);
        }

    }

    @Override
    public void onChildDrawOver(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((AdapterLocalImage.ViewHolder) viewHolder).binding.layoutForeground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((AdapterLocalImage.ViewHolder) viewHolder).binding.layoutForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void clearView(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((AdapterLocalImage.ViewHolder) viewHolder).binding.layoutForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }
}
