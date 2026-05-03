package com.example.crearticket;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportesAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reportes);

        // Inicializar datos de prueba en la UI
        TextView tvFecha = findViewById(R.id.tv_fecha_actual);
        String fechaActual = new SimpleDateFormat("EEEE, d 'de' MMMM yyyy", new Locale("es", "ES")).format(new Date());
        tvFecha.setText(fechaActual);


        // Botón Descargar PDF
        Button btnDescargar = findViewById(R.id.btn_descargar_pdf);
        btnDescargar.setOnClickListener(v -> generarPDFProfesional(fechaActual));
    }

    private void generarPDFProfesional(String fecha) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Configuración de página A4 (aprox)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 1. Encabezado Decorativo (Header)
        paint.setColor(Color.parseColor("#6200EE")); // Color de tu App [cite: 23]
        canvas.drawRect(0, 0, 595, 120, paint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(24f);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Taller de Costura - Reporte General", 40, 60, titlePaint);

        titlePaint.setTextSize(14f);
        titlePaint.setFakeBoldText(false);
        canvas.drawText("Fecha de emisión: " + fecha, 40, 90, titlePaint);

        // 2. Títulos de Secciones
        paint.setColor(Color.BLACK);
        paint.setTextSize(18f);
        paint.setFakeBoldText(true);
        canvas.drawText("Indicadores de Rendimiento", 40, 170, paint);

        // Línea divisoria
        paint.setStrokeWidth(2f);
        canvas.drawLine(40, 180, 555, 180, paint);

        // 3. Cuerpo del Reporte (Información Plasmada)
        paint.setTextSize(14f);
        paint.setFakeBoldText(false);
        int yPos = 220;
        int lineSpacing = 35;

        String[][] datos = {
                {"Métrica", "Cantidad"},
                {"Total de Tickets Generados", "150"},
                {"Tickets en Estado Pendiente", "45"},
                {"Tickets Finalizados/Cerrados", "105"},
                {"Personal/Usuarios Registrados", "8"},
                {"Cartera de Clientes Totales", "215"}
        };

        for (int i = 0; i < datos.length; i++) {
            if (i == 0) paint.setFakeBoldText(true); // Encabezado de tabla
            canvas.drawText(datos[i][0], 50, yPos, paint);
            canvas.drawText(datos[i][1], 450, yPos, paint);

            paint.setFakeBoldText(false);
            paint.setColor(Color.LTGRAY);
            canvas.drawLine(40, yPos + 10, 555, yPos + 10, paint);
            paint.setColor(Color.BLACK);

            yPos += lineSpacing;
        }

        // 4. Pie de Página
        paint.setColor(Color.GRAY);
        paint.setTextSize(10f);
        canvas.drawText("Este reporte es generado automáticamente por el sistema administrativo.", 40, 800, paint);

        pdfDocument.finishPage(page);

        // Guardar el archivo
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Reporte_Taller_Costura.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF guardado en Descargas", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }
}