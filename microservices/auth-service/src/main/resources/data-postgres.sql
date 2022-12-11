
INSERT INTO public.web_shops(
cancel_url, mail, name, password, return_url, shop_id, success_url)
VALUES ('http://localhost:9000/api/orders/cancel', 'shop1@gmail.com', 'shop1', '123', 'http://localhost:4201', '123456789', 'http://localhost:9000/api/orders/confirm');

INSERT INTO public.payment_methods(
name)
VALUES ('PAYPAL');

INSERT INTO public.payment_methods(
 name)
VALUES ('BANK');

INSERT INTO public.payment_methods(
 name)
VALUES ('CRYPTO');

INSERT INTO public.payment_methods(
 name)
VALUES ('QR_CODE');

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 1);

INSERT INTO public.web_shop_payment_method(
shop_id, payment_method_id)
VALUES (1, 2);