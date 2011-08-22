--
-- Table structure for table core_indexer_action_en
--
DROP TABLE IF EXISTS core_indexer_action_en;
CREATE TABLE  core_indexer_action_en (
  id_action int default 0 NOT NULL,
  id_document varchar(255) NOT NULL,
  id_task int default 0 NOT NULL,
  indexer_name varchar(255) NOT NULL,
  id_portlet int default 0 NOT NULL,
  PRIMARY KEY (id_action)
);