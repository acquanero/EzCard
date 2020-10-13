package com.acquanero.ezcard.ezdatabase;

import com.acquanero.ezcard.model.Card;
import com.acquanero.ezcard.model.Provider;

public class ModelToSchemaConverter {

    public ModelToSchemaConverter(){

    }

    public Tarjeta convertCardToTarjeta(Card card){

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdCard(card.getCardId());
        tarjeta.setName(card.getName());
        tarjeta.setIcon(card.getIcon());

        return tarjeta;

    }

    public Proveedor convertProviderToProveedor(Provider provider){

        Proveedor proveedor = new Proveedor();
        proveedor.setIdProvider(provider.getProviderId());
        proveedor.setName(provider.getProviderName());
        proveedor.setCardId(provider.getCardId());
        proveedor.setEnabled(provider.getEnabled());

        return proveedor;

    }

}
