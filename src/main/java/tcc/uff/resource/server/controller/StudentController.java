package tcc.uff.resource.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studant")
public class StudentController {

    //TODO GET PRA PEGAR AS FREQUENCIAS DOS ESTUDANTES DAQUELE CURSO DE UM DETERMINADO MES

    //[
    //		{
    //			"id": "1",
    //			"name": "João Victor Simonassi",
    //			"frequencies": [
    //				{
    //					"date": "2023-06-24T00:00:00.685Z",
    //					"status": 1
    //				},
    //				{
    //					"date": "2023-06-24T12:00:00.685Z",
    //					"status": 1
    //				},
    //				{
    //					"date": "2023-06-28T12:00:00.685Z",
    //					"status": 1
    //				},
    //				{
    //					"date": "2023-07-01T12:00:00.685Z",
    //					"status": 1
    //				}
    //			]
    //		},]

    //TODO: PATCH PATCH PRA ALTERAR O STATUS DA FREQUENCIA DE UM DETERMINADO CURSO DE UM DETERMINA DIA DE UMTEDEMINADO MES

    //PAYLOAD UM STATUS
}
