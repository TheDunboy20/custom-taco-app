package com.taco.customtacoapp;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JdbcOrderRepository implements OrderRepository{
    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository (JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order"
                + "(delivery_Name, delivery_Street, delivery_City, delivery_State, delivery_Zip, cc_number, cc_expiration, cc_cvv, placed_at)"
                + "values (?,?,?,?,?,?,?,?,?)",
                + Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                + Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                + Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
        pscf.setReturnGeneratedKeys(true);

        tacoOrder.setPlacedAt(new Date());
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(tacoOrder.getDeliveryName(),
                        tacoOrder.getDeliveryStreet(),
                        tacoOrder.getDeliveryCity(),
                        tacoOrder.getDeliveryState(),
                        tacoOrder.getDeliveryZip(),
                        tacoOrder.getCcNumber(),
                        tacoOrder.getCcExpiration(),
                        tacoOrder.getCcCVV(),
                        tacoOrder.getPlacedAt()));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);

        List<Taco> tacos = tacoOrder.getTacos();
        int i=0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
        return tacoOrder;
    }
}
