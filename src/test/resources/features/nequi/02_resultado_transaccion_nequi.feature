# language: es
@nequi @resultado
Característica: Resultado final de las transacciones con Nequi
  Como comercio afiliado a Wompi
  Quiero conocer el resultado final de cada transacción Nequi
  Para entregar el producto solo cuando el pago esté aprobado

  # En sandbox Nequi resuelve la transacción de forma asíncrona según el número usado,
  # simulando la respuesta del cliente en su app: 3991111111 aprueba y 3992222222 declina.

  Antecedentes:
    Dado que el comercio tiene sus llaves del ambiente sandbox

  @CP-05 @positivo @smoke
  Escenario: Completar transacción Nequi con resultado aprobado
    Dado que el comercio creó una transacción de 25000 pesos al número Nequi "3991111111"
    Cuando la transacción finaliza su procesamiento
    Entonces la transacción queda en estado "APPROVED"
    Y la transacción registra su fecha de finalización

  @CP-06 @alterno
  Escenario: Completar transacción Nequi con resultado declinado
    Dado que el comercio creó una transacción de 25000 pesos al número Nequi "3992222222"
    Cuando la transacción finaliza su procesamiento
    Entonces la transacción queda en estado "DECLINED" con el mensaje "Transacción RECHAZADA en Sandbox"
    Y la transacción registra su fecha de finalización
