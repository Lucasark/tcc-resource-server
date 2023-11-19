package tcc.uff.resource.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frequency")
public class FrequencyController {

    @GetMapping("/courses/{courseId}/owner")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public Object getQueryFrequency(Authentication authentication,
                                    @PathVariable String courseId) {
    //TODO GET PRA PEGAR AS FREQUENCIAS DOS ESTUDANTES DAQUELE CURSO DE UM DETERMINADO MES
        return null;
    }

//    {
//        "course": {
//        "id": "courseId",
//                "name": "Curso de Java",
//                "members": [
//        {
//            "id": "userId",
//                "name": "João Victor Simonassi",
//                "frequencies": [
//            {
//                "date": "2023-06-24T00:00:00.685Z",
//                    "status": 1
//            },
//            {
//                "date": "2023-06-24T12:00:00.685Z",
//                    "status": 1
//            },
//            {
//                "date": "2023-06-28T12:00:00.685Z",
//                    "status": 1
//            },
//            {
//                "date": "2023-07-01T12:00:00.685Z",
//                    "status": 1
//            }
//                ]
//        }
//        ]
//    }
//    }

    @GetMapping("/courses/{courseId}/member")
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #courseId)")
    public Object getMemberFrequency(Authentication authentication,
                                     @PathVariable String courseId) {
        //TODO GET PARA PEGAR A FREQUENCIA DE UM MEMBRO PELO MEMBRO;
        return null;
    }
    //		{
    //			"id": "1",
    //			"name": "João Victor Simonassi",
    //			"frequencies": [
    //				{
    //                  "id": "frequencyId",
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
    //		}

    @PatchMapping("/frequencies/{frequencyId}/members/{memberId}")
    //TODO: Se eu pertenço ao curso, Se eu sou dono do curso desta frequencia
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #memberId)")
    public Object updateFrequencyByMember(Authentication authentication,
                                          @PathVariable String frequencyId,
                                          @PathVariable String memberId
    ) {
    //TODO: PATCH PATCH PRA ALTERAR O STATUS DA FREQUENCIA DE UM DETERMINADO CURSO DE UM DETERMINA DIA DE UMTEDEMINADO MES
        return null;
    }





    //TODO: PUT de uma nova Presença dentro da Frequencia


}
