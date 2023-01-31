
INSERT INTO public.web_shops(
cancel_url, mail, name, password, return_url, shop_id,shop_secret, success_url)
VALUES ('http://localhost:9000/api/orders/cancel', 'shop1@gmail.com', 'shop1', '$2a$10$0/97GQjGFdPJBX/Bgst/8e2GrFIN/F0A/qk0oxL9PDXUIRLK73x9e', 'http://localhost:4201', '123456789','$2a$12$jF8pl.DA1Xq6gbgHG4Chb.U5XSfwnB3B4p.6Rzy.fzCDPsy8Rr6RW', 'http://localhost:9000/api/orders/confirm');

INSERT INTO public.payment_methods(add_payment_method_url,
name)
VALUES ('https://localhost:8084/subscribe-web-shop','PAYPAL');

INSERT INTO public.payment_methods(add_payment_method_url,
 name)
VALUES ('','BANK');

INSERT INTO public.payment_methods(add_payment_method_url,
 name)
VALUES ('https://localhost:8082/subscribe-web-shop','CRYPTO');

INSERT INTO public.payment_methods(add_payment_method_url,
 name)
VALUES ('','QR_CODE');

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 1);

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 2);

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 3);

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 4);