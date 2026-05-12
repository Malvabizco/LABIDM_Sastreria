package com.example.crearticket;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

public class ReportesAdmin extends AppCompatActivity {

    private TextView tvFecha;
    private TextView tvTotalTickets;
    private TextView tvTicketsAbiertos;
    private TextView tvTicketsCerrados;
    private TextView tvTotalUsuarios;
    private TextView tvTotalClientes;

    private FirebaseFirestore db;

    private int totalTickets = 0;
    private int ticketsAbiertos = 0;
    private int ticketsCerrados = 0;
    private int totalUsuarios = 0;
    private int totalClientes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reportes);

        db = FirebaseFirestore.getInstance();

        tvFecha = findViewById(R.id.tv_fecha_actual);
        tvTotalTickets = findViewById(R.id.tv_total_tickets);
        tvTicketsAbiertos = findViewById(R.id.tv_tickets_abiertos);
        tvTicketsCerrados = findViewById(R.id.tv_tickets_cerrados);
        tvTotalUsuarios = findViewById(R.id.tv_total_usuarios);
        tvTotalClientes = findViewById(R.id.tv_total_clientes);

        String fechaActual = new SimpleDateFormat(
                "EEEE, d 'de' MMMM yyyy",
                new Locale("es", "ES")
        ).format(new Date());

        tvFecha.setText(fechaActual);

        Button btnDescargar = findViewById(R.id.btn_descargar_pdf);
        btnDescargar.setOnClickListener(v -> generarPDFProfesional(fechaActual));

        cargarDatosDesdeFirestore();
    }

    private void cargarDatosDesdeFirestore() {
        cargarTickets();
        cargarUsuarios();
        cargarClientes();
    }

    private void cargarTickets() {
        db.collection("tickets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    totalTickets = queryDocumentSnapshots.size();
                    ticketsAbiertos = 0;
                    ticketsCerrados = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String status = doc.getString("status");

                        if (status != null && status.equalsIgnoreCase("Nuevo")) {
                            ticketsAbiertos++;
                        } else {
                            ticketsCerrados++;
                        }
                    }

                    tvTotalTickets.setText(String.valueOf(totalTickets));
                    tvTicketsAbiertos.setText(String.valueOf(ticketsAbiertos));
                    tvTicketsCerrados.setText(String.valueOf(ticketsCerrados));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar tickets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarUsuarios() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    totalUsuarios = queryDocumentSnapshots.size();
                    tvTotalUsuarios.setText(String.valueOf(totalUsuarios));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarClientes() {
        db.collection("clients")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    totalClientes = queryDocumentSnapshots.size();
                    tvTotalClientes.setText(String.valueOf(totalClientes));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar clientes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void generarPDFProfesional(String fecha) {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        paint.setColor(Color.parseColor("#6200EE"));
        canvas.drawRect(0, 0, 595, 120, paint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(24f);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Taller de Costura - Reporte General", 40, 60, titlePaint);

        titlePaint.setTextSize(14f);
        titlePaint.setFakeBoldText(false);
        canvas.drawText("Fecha de emisión: " + fecha, 40, 90, titlePaint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(18f);
        paint.setFakeBoldText(true);
        canvas.drawText("Indicadores de Rendimiento", 40, 170, paint);

        paint.setStrokeWidth(2f);
        canvas.drawLine(40, 180, 555, 180, paint);

        paint.setTextSize(14f);
        paint.setFakeBoldText(false);

        int yPos = 220;
        int lineSpacing = 35;

        String[][] datos = {
                {"Métrica", "Cantidad"},
                {"Total de Tickets Generados", String.valueOf(totalTickets)},
                {"Tickets Abiertos", String.valueOf(ticketsAbiertos)},
                {"Tickets Cerrados", String.valueOf(ticketsCerrados)},
                {"Usuarios Registrados", String.valueOf(totalUsuarios)},
                {"Clientes Totales", String.valueOf(totalClientes)}
        };

        for (int i = 0; i < datos.length; i++) {
            paint.setFakeBoldText(i == 0);
            paint.setColor(Color.BLACK);

            canvas.drawText(datos[i][0], 50, yPos, paint);
            canvas.drawText(datos[i][1], 450, yPos, paint);

            paint.setColor(Color.LTGRAY);
            canvas.drawLine(40, yPos + 10, 555, yPos + 10, paint);

            yPos += lineSpacing;
        }

        paint.setColor(Color.GRAY);
        paint.setTextSize(10f);
        paint.setFakeBoldText(false);
        canvas.drawText("Este reporte es generado automáticamente por el sistema administrativo.", 40, 800, paint);

        pdfDocument.finishPage(page);

        File carpeta = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (carpeta == null) {
            Toast.makeText(this, "No se pudo acceder al almacenamiento", Toast.LENGTH_SHORT).show();
            pdfDocument.close();
            return;
        }

        String fechaArchivo = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
        ).format(new Date());

        File file = new File(carpeta, "Reporte_Taller_Costura_" + fechaArchivo + ".pdf");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            pdfDocument.writeTo(outputStream);
            outputStream.close();
            pdfDocument.close();

            Toast.makeText(this, "PDF generado correctamente", Toast.LENGTH_SHORT).show();
            abrirPDF(file);

        } catch (IOException e) {
            pdfDocument.close();
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirPDF(File file) {
        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No tienes una app para abrir PDF", Toast.LENGTH_LONG).show();
        }
    }
}