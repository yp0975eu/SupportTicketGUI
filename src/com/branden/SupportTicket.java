package com.branden;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

public class SupportTicket extends JFrame{
    private JTextField enterSupportTicketTextField;
    private JPanel rootPanel;
    private JList<Ticket> openTickets;
    private JList<ResolvedTicket> closedTickets;
    private JTextField resolutionTextField;
    private JButton closeTicketButton;
    private JButton addTicketButton;
    private JTextField reporterTextField;
    private JTextField priorityTextField;
    private DefaultListModel<Ticket> openTicketsModel;
    private DefaultListModel<ResolvedTicket> resolvedTicketsModel;

    SupportTicket(){
        super("Support Ticket");
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        openTicketsModel = new DefaultListModel();
        resolvedTicketsModel = new DefaultListModel();
        openTickets.setModel(openTicketsModel);
        closedTickets.setModel(resolvedTicketsModel);
        openTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String issue = enterSupportTicketTextField.getText();
                String reporter = reporterTextField.getText();
                int priority;
                try {
                    priority = Integer.parseInt( priorityTextField.getText() );
                } catch (Exception er){
                    return;
                }
                // clear text fields and add to ticket list
                enterSupportTicketTextField.setText("");
                reporterTextField.setText("");
                priorityTextField.setText("");
                addTicketInPriorityOrder(openTicketsModel, new Ticket(issue, priority, reporter, new Date() ) );
                pack();
            }
        });
        closeTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket ticket;
                int selectedIndex = openTickets.getSelectedIndex(); //Ask the LIST what is selected
                String resolution = resolutionTextField.getText();
                if (selectedIndex != -1 && resolution.trim().length() > 0) {

                    // add to resolved tickets
                    ticket = openTicketsModel.get(selectedIndex);  //Get this item from the MODEL.
                    resolvedTicketsModel.addElement( new ResolvedTicket(resolution, new Date(), ticket) );
                    openTicketsModel.remove(selectedIndex);  //Get this item from the MODEL.

                    // clear textfield
                    resolutionTextField.setText("");
                    pack();
                }

            }
        });
    }
    protected static void addTicketInPriorityOrder(DefaultListModel<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.addElement(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addElement(newTicket);
    }
}
