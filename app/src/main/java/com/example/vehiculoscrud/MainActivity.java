package com.example.vehiculoscrud;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
    
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewVehiculos;
    private Button btnAgregar;
    private List<Vehiculo> listaVehiculos;
    private ArrayAdapter<Vehiculo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper          = new DatabaseHelper(this);
        listViewVehiculos = findViewById(R.id.listViewVehiculos);
        btnAgregar        = findViewById(R.id.btnAgregar);

        // Inicializar lista y adaptador una sola vez
        listaVehiculos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaVehiculos);
        listViewVehiculos.setAdapter(adapter);

        cargarLista();

        // Botón AGREGAR
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Click en item -> Editar / Eliminar
        listViewVehiculos.setOnItemClickListener((parent, view, position, id) -> {
            Vehiculo v = listaVehiculos.get(position);
            mostrarOpcionesVehiculo(v);
        });
    }

    private void cargarLista() {
        listaVehiculos.clear();
        listaVehiculos.addAll(dbHelper.obtenerTodosLosVehiculos());
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoAgregar() {
        View formView = LayoutInflater.from(this).inflate(R.layout.dialog_vehiculo, null);

        EditText etMarca  = formView.findViewById(R.id.etMarca);
        EditText etModelo = formView.findViewById(R.id.etModelo);
        EditText etAnio   = formView.findViewById(R.id.etAnio);
        EditText etPlaca  = formView.findViewById(R.id.etPlaca);
        EditText etColor  = formView.findViewById(R.id.etColor);

        new AlertDialog.Builder(this)
                .setTitle("➕ Agregar Vehículo")
                .setView(formView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String marca  = etMarca.getText().toString().trim();
                    String modelo = etModelo.getText().toString().trim();
                    String anio   = etAnio.getText().toString().trim();
                    String placa  = etPlaca.getText().toString().trim();
                    String color  = etColor.getText().toString().trim();

                    if (marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || placa.isEmpty() || color.isEmpty()) {
                        Toast.makeText(this, "⚠️ Error: Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean ok = dbHelper.insertarVehiculo(marca, modelo, anio, placa, color);
                    if (ok) {
                        Toast.makeText(this, "✅ Vehículo guardado con éxito", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    } else {
                        Toast.makeText(this, "❌ Error: La placa ya existe", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarOpcionesVehiculo(Vehiculo vehiculo) {
        new AlertDialog.Builder(this)
                .setTitle("Seleccione una acción")
                .setItems(new String[]{"✏️ Editar datos", "🗑️ Eliminar vehículo"}, (dialog, which) -> {
                    if (which == 0) mostrarDialogoEditar(vehiculo);
                    else            confirmarEliminar(vehiculo);
                })
                .show();
    }

    private void mostrarDialogoEditar(Vehiculo vehiculo) {
        View formView = LayoutInflater.from(this).inflate(R.layout.dialog_vehiculo, null);

        EditText etMarca  = formView.findViewById(R.id.etMarca);
        EditText etModelo = formView.findViewById(R.id.etModelo);
        EditText etAnio   = formView.findViewById(R.id.etAnio);
        EditText etPlaca  = formView.findViewById(R.id.etPlaca);
        EditText etColor  = formView.findViewById(R.id.etColor);

        etMarca.setText(vehiculo.getMarca());
        etModelo.setText(vehiculo.getModelo());
        etAnio.setText(vehiculo.getAnio());
        etPlaca.setText(vehiculo.getPlaca());
        etColor.setText(vehiculo.getColor());

        new AlertDialog.Builder(this)
                .setTitle("✏️ Editar Vehículo")
                .setView(formView)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String marca  = etMarca.getText().toString().trim();
                    String modelo = etModelo.getText().toString().trim();
                    String anio   = etAnio.getText().toString().trim();
                    String placa  = etPlaca.getText().toString().trim();
                    String color  = etColor.getText().toString().trim();

                    if (marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || placa.isEmpty() || color.isEmpty()) {
                        Toast.makeText(this, "⚠️ Error: No deje campos vacíos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean ok = dbHelper.actualizarVehiculo(vehiculo.getId(), marca, modelo, anio, placa, color);
                    if (ok) {
                        Toast.makeText(this, "✅ Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    } else {
                        Toast.makeText(this, "❌ Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminar(Vehiculo vehiculo) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea borrar el " + vehiculo.getMarca() + " con placa " + vehiculo.getPlaca() + "?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    boolean ok = dbHelper.eliminarVehiculo(vehiculo.getId());
                    if (ok) {
                        Toast.makeText(this, "✅ Vehículo eliminado", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
