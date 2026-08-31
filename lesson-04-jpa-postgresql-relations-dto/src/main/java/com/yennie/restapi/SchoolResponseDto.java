package com.yennie.restapi;

/** 返回给客户端的学校数据，包括数据库生成的 ID。 */
public record SchoolResponseDto(Integer id, String name) {
}
