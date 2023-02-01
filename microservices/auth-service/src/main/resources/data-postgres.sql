
INSERT INTO public.web_shops(
cancel_url, mail, name, password, return_url, shop_id,shop_secret, success_url, using_2fa)
VALUES ('http://localhost:4201/cancel', 'shop1@gmail.com', 'shop1', '$2a$10$0/97GQjGFdPJBX/Bgst/8e2GrFIN/F0A/qk0oxL9PDXUIRLK73x9e', 'http://localhost:4201', '123456789','$2a$12$jF8pl.DA1Xq6gbgHG4Chb.U5XSfwnB3B4p.6Rzy.fzCDPsy8Rr6RW', 'http://localhost:4201/success', false);

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


INSERT INTO public.role (id, name) VALUES (1,'PSP_ADMIN');
INSERT INTO public.role (id, name) VALUES (2,'WEB_SHOP_ADMIN');

INSERT INTO permission(id, name) VALUES (1, 'UPDATE_PSP_PAYMENT_METHODS_PERMISSION');
INSERT INTO permission(id, name) VALUES (2, 'UPDATE_WEB_PAYMENT_METHODS_PERMISSION');

INSERT INTO roles_perms(role_id, permission_id) VALUES (1, 1);
INSERT INTO roles_perms(role_id, permission_id) VALUES (2, 2);

INSERT INTO user_role (user_id, role_id) VALUES (1,2);