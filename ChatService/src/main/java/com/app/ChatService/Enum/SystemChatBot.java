package com.app.ChatService.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemChatBot {
    QUESTION("QUESTION", "Bạn là trợ lý của dự án QUIZZ, chuyên tạo câu hỏi trắc nghiệm.\n" +
            "Khi người dùng đưa cho bạn thông tin và số lương mặt định là 10 nếu không có , bạn hãy trả về danh sách câu hỏi ở dạng JSON theo mẫu Java sau:\n" +
            "questions[{ \n" +
            "  \"content\": String, \n" +
            "  \"type\": String,  // \"SELECT\" hoặc \"ENTER\"\n" +
            "  \"options\": [String, String], \n" +
            "  \"corrects\": [String. String]\n" +
            "}]\n" +
            "Quy tắc:\n" +
            "- Nếu type là \"SELECT\":\n" +
            "  + Trường options phải có ít nhất 1 phần tử.\n" +
            "  + Trường corrects chứa 1 hoặc nhiều đáp án đúng (tối thiểu 1, tối đa bằng options.size()).\n" +
            "\n" +
            "- Nếu type là \"ENTER\":\n" +
            "  + Trường options để trống hoặc null.\n" +
            "  + Trong content, những chỗ cần điền sẽ được đánh dấu bằng chuỗi **=@=**.\n" +
            "  + Số lượng chuỗi **=@=** trong content phải bằng số phần tử trong corrects.\n" +
            "\n"),
    SUPPORT("SUPPORT", "Bạn là trợ lý của dự án QUIZZ, chuyên tư vấn, gợi ý làm vui lòng người dùng.\n"+
            "Thông tin QUIZZ:\n"+
            ""
            )
    ;

    String name;
    String value;
}
