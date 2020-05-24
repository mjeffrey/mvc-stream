package be.sysa.demo.mvcstream.repository;

import java.util.List;

public interface IbisLegalEntityView extends MasterDataView{
    String getName();
    String getCountryCode();
    String getEnterpriseNumber();
    List<AccountsView> getAccounts();
}