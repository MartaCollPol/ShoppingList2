package edu.upc.eseiaat.pma.shoppinglist2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<ShoppingItem> itemlist;
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //Per crear variables privades directament un cop declarades com a locals
        //ens posem a sobre del nom d'aquesta i li donem a ctrl+alt+f i a current method.

        list = (ListView) findViewById(R.id.list);
        btn_add = (Button) findViewById(R.id.btn_add);
        edit_item = (EditText)findViewById(R.id.edit_item);

        itemlist= new ArrayList<>();
        itemlist.add(new ShoppingItem("Los"));
        itemlist.add(new ShoppingItem("Catalanes",true));
        itemlist.add(new ShoppingItem("Hacen"));
        itemlist.add(new ShoppingItem("Cosas"));

        // Cada item de la llista té el seu layout en aquest cas per a un ArrayAdapter s'utilitza simple_list_item_1 layout.
        //Per a canviar a un layout personalitzat necesitem fer el nostre propi adaptador per a que controli el nou layout.
        adapter = new ShoppingListAdapter(this, android.R.layout.simple_list_item_1, itemlist);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addItem();
                return false;
            }
        });

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id){
                //volem que al treure un Check es guardi que l'item de la posició pos no estar checked
                itemlist.get(pos).toggleChecked();// get invoca el getter de la classe shoping list, igual que togglecheck invoca aquest mètode.
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return false;
            }
        });

    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String s = getResources().getString(R.string.confirm_message);
        builder.setMessage(String.format(s,itemlist.get(pos).getText()));
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemlist.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();

    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if(!item_text.isEmpty()){
            itemlist.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
        }
        //métode que fa que al afagir un item es fagi un autoscroll per a veure que s'ha afegit quelcom.
        list.smoothScrollToPosition(itemlist.size()-1);

        //TODO: solucionar que es guardin les coses al rotar el dispositiu i que els elements es guardin al apagar el dispositiu.

    }
}
