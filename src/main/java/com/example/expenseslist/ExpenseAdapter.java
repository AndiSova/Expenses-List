package com.example.expenseslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense>
{
    private Context mContext;
    private List<Expense> contactList = new ArrayList<Expense>();

    public ExpenseAdapter( Context context, ArrayList<Expense> list)
    {
        super( context, 0, list);
        mContext = context;
        contactList = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        String change;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.expense_view,parent,false);

        Expense currentExpense = contactList.get(position);

        TextView item = (TextView) listItem.findViewById(R.id.textView_Item);
        item.setText(currentExpense.getItem());


        TextView price = (TextView) listItem.findViewById(R.id.textView_Price);

        if ( !currentExpense.getPrice().equals(""))
            change = "P:$" + currentExpense.getPrice();
        else
            change = currentExpense.getPrice();
        price.setText(change);


        if ( !currentExpense.getDate().equals(""))
            change = "D:" + currentExpense.getDate();
        else
            change = currentExpense.getDate();
        TextView date = (TextView) listItem.findViewById(R.id.textView_Date);
        date.setText(change);


        if ( !currentExpense.getQuantity().equals(""))
            change = "Q:" + currentExpense.getQuantity();
        else
            change = currentExpense.getQuantity();
        TextView quantity = (TextView) listItem.findViewById(R.id.textView_Quantity);
        quantity.setText(change);

        return listItem;
    }
}
