package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;

import java.time.Instant;
import java.util.List;

public interface CourseRepository extends MongoRepository<CourseDocument, String>, CustomizedCourseRepository {

    List<CourseDocument> findByTeacherEmail(String user);

    List<CourseDocument> findByMembersEmail(String user);

    @Aggregation(pipeline = {
            "{$match: {_id: {$in: [ObjectId(?0)]}}}",
            "{$unwind: '$members'}",
            "{$lookup: {from: 'users', localField: 'members.$id', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
            "{$lookup: {from: 'frequency', localField: '_id', foreignField: 'course.$id', as: 'frequencyDetails'}}",
            "{$addFields: {frequenciesWithAttendances: {$filter: {input: '$frequencyDetails', as: 'frequency', cond: {" +
                    "$and: [{$isArray: '$$frequency.attendances'}," +
                    "{$gt: [{$size: '$$frequency.attendances'}, 0]}," +
                    "{$ne: [{$type: {$arrayElemAt: ['$$frequency.attendances.student', 0]}}, 'missing']}]" +
                    "}}}}}",
            "{$addFields: {updatedAliases: {$map: {input: '$user.aliases', as: 'alias', in: {" +
                    "$mergeObjects: ['$$alias', {courseId: {$toObjectId: '$$alias.courseId'}}]" +
                    "}}}}}",
            "{$addFields: {userMatchedAlias: {$filter: {input: '$updatedAliases', as: 'alias', cond: {$eq: ['$$alias.courseId', '$_id']}}}}}",
            "{$addFields: {alias: {$cond: {if: {$eq: [{$size: '$userMatchedAlias'}, 0]}, then: 'S/A', else: {$arrayElemAt: ['$userMatchedAlias.name', 0]}}}}}",
            "{$addFields: {frequenciesWithMatchingAttendances: {$filter: {input: '$frequenciesWithAttendances', as: 'frequency', cond: {" +
                    "$gt: [{$size: {$filter: {input: '$$frequency.attendances', as: 'attendance', cond: {" +
                    "$eq: ['$$attendance.student.$id', '$user._id']}}}}, 0]}" +
                    "}}}}",
            "{$addFields: {frequenciesWithMatchingAttendances: {$map: {input: '$frequenciesWithMatchingAttendances', as: 'frequency', in: {" +
                    "id: {$toString: '$$frequency._id'}," +
                    "date: '$$frequency.date'," +
                    "status: {$arrayElemAt: [{$map: {input: {$filter: {input: '$$frequency.attendances', as: 'attendance', cond: {$eq: ['$$attendance.student.$id', '$user._id']},}}, as: 'filteredAttendance', in: '$$filteredAttendance.status'}}, 0]" +
                    "}}}}}}",
            "{$project: {_id: 0, id: {$toString :'$user._id'}, alias: '$alias', frequencies: '$frequenciesWithMatchingAttendances'}}"
    })
    List<FrequencyMapperResponse> getViewFrequencyByCourseId(String courseId);

    @Aggregation(pipeline = {
            "{$match: {_id: {$in: [ObjectId(?0)]}}}",
            "{$unwind: '$members'}",
            "{$lookup: {from: 'users', localField: 'members.$id', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
            "{$lookup: {from: 'frequency', localField: '_id', foreignField: 'course.$id', as: 'frequencyDetails'}}",
            "{$addFields: {frequenciesWithAttendances: {$filter: {input: '$frequencyDetails', as: 'frequency', cond: {" +
                    "$and: [{$isArray: '$$frequency.attendances'}," +
                    "{$gte: ['$$frequency.date', ?1]}," +
                    "{$lte: ['$$frequency.date', ?2]}," +
                    "{$gt: [{$size: '$$frequency.attendances'}, 0]}," +
                    "{$ne: [{$type: {$arrayElemAt: ['$$frequency.attendances.student', 0]}}, 'missing']}]" +
                    "}}}}}",
            "{$addFields: {updatedAliases: {$map: {input: '$user.aliases', as: 'alias', in: {" +
                    "$mergeObjects: ['$$alias', {courseId: {$toObjectId: '$$alias.courseId'}}]" +
                    "}}}}}",
            "{$addFields: {userMatchedAlias: {$filter: {input: '$updatedAliases', as: 'alias', cond: {$eq: ['$$alias.courseId', '$_id']}}}}}",
            "{$addFields: {alias: {$cond: {if: {$eq: [{$size: '$userMatchedAlias'}, 0]}, then: 'S/A', else: {$arrayElemAt: ['$userMatchedAlias.name', 0]}}}}}",
            "{$addFields: {frequenciesWithMatchingAttendances: {$filter: {input: '$frequenciesWithAttendances', as: 'frequency', cond: {" +
                    "$gt: [{$size: {$filter: {input: '$$frequency.attendances', as: 'attendance', cond: {" +
                    "$eq: ['$$attendance.student.$id', '$user._id']}}}}, 0]}" +
                    "}}}}",
            "{$addFields: {frequenciesWithMatchingAttendances: {$map: {input: '$frequenciesWithMatchingAttendances', as: 'frequency', in: {" +
                    "id: {$toString: '$$frequency._id'}," +
                    "date: '$$frequency.date'," +
                    "status: {$arrayElemAt: [{$map: {input: {$filter: {input: '$$frequency.attendances', as: 'attendance', cond: {$eq: ['$$attendance.student.$id', '$user._id']},}}, as: 'filteredAttendance', in: '$$filteredAttendance.status'}}, 0]" +
                    "}}}}}}",
            "{$project: {_id: 0, id: {$toString :'$user._id'}, alias: '$alias', frequencies: '$frequenciesWithMatchingAttendances'}}"
    })
    List<FrequencyMapperResponse> getViewFrequencyByCourseIdAndDate(String courseId, Instant start, Instant end);
}
