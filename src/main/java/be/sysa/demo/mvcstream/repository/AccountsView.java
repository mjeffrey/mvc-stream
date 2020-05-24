package be.sysa.demo.mvcstream.repository;

public interface AccountsView extends MasterDataView{

    String getIban();
    String getAccountType();
    String getCurrencyCode();
}
