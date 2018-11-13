--Populate promotion and coupon tables
insert promotion values(null, 'Promocao 1', CURDATE(), CURDATE()+1);
insert coupon values(null, 'Cupom 1 da promocao 1', 10, '1022112233');
insert promotion_coupons values(1,1);

insert promotion values(null, 'Promocao 2', CURDATE()+1, CURDATE()+2);
insert coupon values(null, 'Cupom 2 da promocao 2', 20, '20221122332');
insert promotion_coupons values(2,2);

--Populate store and promotions
insert store values(null, 'Rua Teste 1', 'Cidade teste1', 0, 0, 'Loja 1', 0, 'PI');
insert store_promotion_list values(1,1);
insert store_promotion_list values(1,2);

--Populate some stores from Fortaleza
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7260311,-38.5047165, 'Boteco Praia', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7241736,-38.5042015, 'Habibs Praia de Iracema', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7244947,-38.5028738, 'Café Vida', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7261007,-38.5022274, 'Koni Street Japanese', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7255493,-38.499395, 'Acarajé Cia', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7255493,-38.499395, 'Barraca da Boa', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7267484,-38.4984938, 'Didi Rei dos Mares', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7267484,-38.4995881, 'Sabor de Mar', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7283864,-38.4974209, 'Bistrô Garrafeira', 0, 'Ceará');
insert store values(null, 'Rua Teste', 'Fortaleza', -3.7283864,-38.4974209, 'Empório Delitalia', 0, 'Ceará');