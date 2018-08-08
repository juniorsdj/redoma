SELECT i.name AS index_name
    ,i.type_desc
    ,is_unique
    ,ds.type_desc AS filegroup_or_partition_scheme
    ,ds.name AS filegroup_or_partition_scheme_name
    ,ignore_dup_key
    ,is_primary_key
    ,is_unique_constraint
    ,fill_factor
    ,is_padded
    ,is_disabled
    ,allow_row_locks
    ,allow_page_locks
FROM sys.indexes AS i
INNER JOIN sys.data_spaces AS ds ON i.data_space_id = ds.data_space_id
WHERE is_hypothetical = 0 AND i.index_id <> 0 
AND i.object_id = OBJECT_ID('Person.Access');
GO