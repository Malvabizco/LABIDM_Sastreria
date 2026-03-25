package com.example.crearticket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crearticket.R;
import com.example.crearticket.models.Ticket;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {

    private List<Ticket> ticketList;
    private OnTicketClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    public TicketsAdapter(List<Ticket> ticketList, OnTicketClickListener listener) {
        this.ticketList = ticketList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.tvClientName.setText(ticket.getClientName());
        holder.tvTypeWork.setText(ticket.getTypeWork() + " - " + ticket.getTypeGarment());
        
        if (ticket.getDeliveryDate() != null) {
            holder.tvDate.setText("Entrega: " + dateFormat.format(ticket.getDeliveryDate()));
        }

        holder.tvStatus.setText(ticket.getStatus());
        
        // Color del estado
        int colorRes = R.color.purple_main;
        switch (ticket.getStatus().toLowerCase()) {
            case "nuevo": colorRes = R.color.status_nuevo; break;
            case "pendiente": colorRes = R.color.status_progreso; break;
            case "falta material": colorRes = R.color.status_progreso; break;
            case "terminado": colorRes = R.color.status_completado; break;
            case "cancelado": colorRes = R.color.status_entregado; break;
        }
        holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), colorRes));

        holder.itemView.setOnClickListener(v -> listener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName, tvTypeWork, tvDate, tvStatus;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvTypeWork = itemView.findViewById(R.id.tvWorkType);
            tvDate = itemView.findViewById(R.id.tvDeliveryDate);
            tvStatus = itemView.findViewById(R.id.tvStatusLabel);
        }
    }
}