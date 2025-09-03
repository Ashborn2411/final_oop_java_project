package util;

import Gui.*;
import Manager.LibraryManager;

import java.util.function.Supplier;

public class Extra {
    private static LibraryManager manager;
   public Extra(LibraryManager manager)
   {
       Extra.manager =manager;
   }
   public static final Supplier[] panelSuppliers = new Supplier[]{
            () -> new BookPanel(manager),
            () -> new MemberPanel(manager),
            () -> new LoanPanel(manager),
            () -> new LibraryCardPanel(manager),
            () -> new PublisherPanel(manager),
            () -> new AuthorPanel(manager)
    };
}
