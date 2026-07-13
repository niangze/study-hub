            // --- Likes (sample) ---
            Object[][] likes = {
                {1, 1, "QUESTION"}, {2, 1, "QUESTION"}, {3, 1, "QUESTION"},
                {1, 2, "QUESTION"}, {2, 3, "QUESTION"}, {3, 3, "QUESTION"},
                {1, 4, "QUESTION"}, {4, 5, "QUESTION"}, {5, 8, "QUESTION"},
                {1, 1, "ANSWER"}, {2, 1, "ANSWER"}, {3, 4, "ANSWER"}, {4, 5, "ANSWER"}, {5, 6, "ANSWER"}
            };
            for (Object[] like : likes) {
                Like l = new Like();
                l.setUserId(((Number)like[0]).longValue());
                l.setTargetId(((Number)like[1]).longValue());
                l.setTargetType((String)like[2]);
                l.setCreatedAt(LocalDateTime.now());
                likeMapper.insert(l);
            }