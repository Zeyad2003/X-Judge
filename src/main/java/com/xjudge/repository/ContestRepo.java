package com.xjudge.repository;

import com.xjudge.entity.Contest;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.enums.ContestVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepo extends JpaRepository<Contest, Long> {


    @Query(
            value = "SELECT c FROM Contest c " +
                    "JOIN c.users u " +
                    "WHERE (:title IS NULL OR :title = '' OR c.title = :title ) " +
                    "AND (:owner IS NULL OR :owner = '' OR (u.user.handle = :owner AND u.isOwner)) " +
                    "order by c.id DESC "
    )

    Page<Contest> searchByOwnerAndTitle(@Param("owner") String owner , @Param("title") String title , Pageable pageable);

    Page<Contest> findContestByVisibility(ContestVisibility visibility , Pageable pageable);

    Page<Contest> findContestByType(ContestType contestType , Pageable pageable);

    List<Contest> findContestsByType(ContestType contestType);

    List<Contest> findContestsByVisibility(ContestVisibility visibility);

    @Query(
             value = "SELECT c FROM Contest c " +
                     "JOIN c.users u " +
                     "Where u.user.handle = :handle " +
                     "order by c.id DESC "
    )
    Page<Contest> findContestsByUser(@Param("handle") String handle , Pageable pageable);

    @Query(
            value = "SELECT c FROM Contest c " +
                    "JOIN c.users u " +
                    "Where u.user.handle = :handle " +
                    "order by c.id DESC "
    )
    List<Contest> findContestsByUser(@Param("handle") String handle);


    @Query (value = "SELECT c FROM Contest c " +
            "JOIN c.users uc " +
            "JOIN uc.user u " +
            "WHERE (c.visibility = :category or c.type = :category or u.handle = :category or :category= '')" +
            "AND ((:title IS NULL OR :title = '' OR c.title like %:title%) " +
            "AND  (:owner IS NULL OR :owner = '' OR (u.handle = :owner AND uc.isOwner))) " +
            "order by c.id DESC "

           )
    Page<Contest> searchByVisibilityOrTypeOrUserAndOwnerAndTitle(@Param("category") String category , @Param("owner") String owner , @Param("title") String title , Pageable pageable);

    @Query (value = "SELECT c FROM Contest c " +
            "JOIN c.users uc " +
            "JOIN uc.user u " +
            "WHERE (c.visibility = :category or c.type = :category or u.handle = :category or :category= '')" +
            "AND ((:title IS NULL OR :title = '' OR c.title like %:title%) " +
            "AND  (:owner IS NULL OR :owner = '' OR (u.handle = :owner AND uc.isOwner))) " +
            "order by c.id DESC "

    )
    List<Contest> searchByVisibilityOrTypeOrUserAndOwnerAndTitle(@Param("category") String category , @Param("owner") String owner , @Param("title") String title);
}
