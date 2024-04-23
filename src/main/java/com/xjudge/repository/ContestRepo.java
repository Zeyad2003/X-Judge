package com.xjudge.repository;

import com.xjudge.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepo extends JpaRepository<Contest, Long> {
    Page<Contest> findContestsByTitleIgnoreCase(String title , Pageable pageable);

    @Query(
            nativeQuery = true ,
            value = "SELECT c.* FROM contest c " +
                    "INNER JOIN user_contest uc ON c.id = uc.contest_id " +
                    "INNER JOIN user u ON u.id = uc.user_id " +
                    "WHERE (IFNULL(:title, '') = '' OR c.title LIKE CONCAT('%', :title, '%')) " +
                    "AND (IFNULL(:owner, '') = ''  OR u.handle = :owner)"
    )

    Page<Contest> searchByOwnerAndTitle(@Param("owner") String owner , @Param("title") String title , Pageable pageable);
}
//(u.handle = :owner and uc.is_owner=1) OR c.title = :title