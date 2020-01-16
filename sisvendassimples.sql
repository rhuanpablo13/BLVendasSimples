# SQL-Front 5.1  (Build 4.16)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: sisvendassimples
# ------------------------------------------------------
# Server version 5.5.48

DROP DATABASE IF EXISTS `sisvendassimples`;
CREATE DATABASE `sisvendassimples` /*!40100 DEFAULT CHARACTER SET cp850 */;
USE `sisvendassimples`;

#
# Source for table carrinho
#

DROP TABLE IF EXISTS `carrinho`;
CREATE TABLE `carrinho` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `ID_VENDA` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CARRINHO_ID_VENDA` (`ID_VENDA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table carrinho
#

LOCK TABLES `carrinho` WRITE;
/*!40000 ALTER TABLE `carrinho` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrinho` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table carrinho_produto
#

DROP TABLE IF EXISTS `carrinho_produto`;
CREATE TABLE `carrinho_produto` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_PRODUTO` int(11) DEFAULT NULL,
  `QUANTIDADE` double DEFAULT '0',
  `ID_CARRINHO` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CARRINHO_PRODUTO_ID_PRODUTO` (`ID_PRODUTO`),
  KEY `FK_CARRINHO_PRODUTO_ID_CARRINHO` (`ID_CARRINHO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table carrinho_produto
#

LOCK TABLES `carrinho_produto` WRITE;
/*!40000 ALTER TABLE `carrinho_produto` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrinho_produto` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table cliente
#

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ENDERECO` varchar(255) DEFAULT NULL,
  `BAIRRO` varchar(60) DEFAULT NULL,
  `CIDADE` varchar(60) DEFAULT NULL,
  `UF` varchar(2) DEFAULT NULL,
  `CEP` varchar(15) DEFAULT NULL,
  `TELEFONE` varchar(20) DEFAULT NULL,
  `CELULAR` varchar(20) DEFAULT NULL,
  `ATIVO` tinyint(1) DEFAULT NULL,
  `SEXO` varchar(12) DEFAULT NULL,
  `EMAIL` varchar(30) DEFAULT NULL,
  `CPF` varchar(20) DEFAULT NULL,
  `RG` varchar(10) DEFAULT NULL,
  `DT_NASCIMENTO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

#
# Dumping data for table cliente
#

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table codigos_uf
#

DROP TABLE IF EXISTS `codigos_uf`;
CREATE TABLE `codigos_uf` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO_IBGE` int(5) DEFAULT NULL,
  `ESTADO` varchar(60) DEFAULT NULL,
  `SIGLA` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO_IBGE` (`CODIGO_IBGE`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=cp850;

#
# Dumping data for table codigos_uf
#

LOCK TABLES `codigos_uf` WRITE;
/*!40000 ALTER TABLE `codigos_uf` DISABLE KEYS */;
INSERT INTO `codigos_uf` VALUES (1,11,'Rondônia','RO');
INSERT INTO `codigos_uf` VALUES (2,12,'Acre','AC');
INSERT INTO `codigos_uf` VALUES (3,13,'Amazonas','AM');
INSERT INTO `codigos_uf` VALUES (4,14,'Roraima','RR');
INSERT INTO `codigos_uf` VALUES (5,15,'Pará','PA');
INSERT INTO `codigos_uf` VALUES (6,16,'Amapá','AP');
INSERT INTO `codigos_uf` VALUES (7,17,'Tocantins','TO');
INSERT INTO `codigos_uf` VALUES (8,21,'Maranhão','MA');
INSERT INTO `codigos_uf` VALUES (9,22,'Piauí','PI');
INSERT INTO `codigos_uf` VALUES (10,23,'Ceará','CE');
INSERT INTO `codigos_uf` VALUES (11,24,'Rio Grande do Norte','RN');
INSERT INTO `codigos_uf` VALUES (12,25,'Paraíba','PB');
INSERT INTO `codigos_uf` VALUES (13,26,'Pernambuco','PE');
INSERT INTO `codigos_uf` VALUES (14,27,'Alagoas','AL');
INSERT INTO `codigos_uf` VALUES (15,28,'Sergipe','SE');
INSERT INTO `codigos_uf` VALUES (16,29,'Bahia','BA');
INSERT INTO `codigos_uf` VALUES (17,31,'Minas Gerais','MG');
INSERT INTO `codigos_uf` VALUES (18,32,'Espírito Santo','ES');
INSERT INTO `codigos_uf` VALUES (19,33,'Rio de Janeiro','RJ');
INSERT INTO `codigos_uf` VALUES (20,35,'São Paulo','SP');
INSERT INTO `codigos_uf` VALUES (21,41,'Paraná','PR');
INSERT INTO `codigos_uf` VALUES (22,42,'Santa Catarina','SC');
INSERT INTO `codigos_uf` VALUES (23,43,'Rio Grande do Sul','RS');
INSERT INTO `codigos_uf` VALUES (24,50,'Mato Grosso do Sul','MS');
INSERT INTO `codigos_uf` VALUES (25,51,'Mato Grosso','MT');
INSERT INTO `codigos_uf` VALUES (26,52,'Goiás','GO');
INSERT INTO `codigos_uf` VALUES (27,53,'Distrito Federal','DF');
/*!40000 ALTER TABLE `codigos_uf` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table empresa
#

DROP TABLE IF EXISTS `empresa`;
CREATE TABLE `empresa` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `RAZAO_SOCIAL` varchar(255) DEFAULT NULL,
  `NOME_FANTASIA` varchar(255) DEFAULT NULL,
  `PRODUTOR_RURAL` tinyint(1) DEFAULT NULL,
  `INSCRICAO_ESTADUAL` int(11) DEFAULT NULL,
  `INSCRICAO_MUNICIPAL` int(11) DEFAULT NULL,
  `CNPJ` varchar(20) DEFAULT NULL,
  `CPF` varchar(20) DEFAULT NULL,
  `MUNICIPIO` varchar(255) DEFAULT NULL,
  `IBGE` varchar(15) DEFAULT NULL,
  `BAIRRO` varchar(60) DEFAULT NULL,
  `CIDADE` varchar(60) DEFAULT NULL,
  `LOGRADOURO` varchar(60) DEFAULT NULL,
  `UF` varchar(2) DEFAULT NULL,
  `NUMERO` int(10) DEFAULT NULL,
  `CEP` varchar(15) DEFAULT NULL,
  `CRT` varchar(60) DEFAULT NULL,
  `TELEFONE` varchar(20) DEFAULT NULL,
  `CELULAR` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(30) DEFAULT NULL,
  `CERTIFICADO` varchar(255) DEFAULT NULL,
  `SENHA` varchar(255) DEFAULT NULL,
  `LOGOMARCA` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table empresa
#

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table forma_pagamento
#

DROP TABLE IF EXISTS `forma_pagamento`;
CREATE TABLE `forma_pagamento` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRICAO` varchar(50) NOT NULL,
  `ENTRADA` int(11) DEFAULT NULL,
  `PARCELAS` int(11) DEFAULT NULL,
  `ID_VENDA` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FORMA_PAGAMENTO_ID_VENDA` (`ID_VENDA`)
) ENGINE=InnoDB DEFAULT CHARSET=cp850;

#
# Dumping data for table forma_pagamento
#

LOCK TABLES `forma_pagamento` WRITE;
/*!40000 ALTER TABLE `forma_pagamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `forma_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table fornecedor
#

DROP TABLE IF EXISTS `fornecedor`;
CREATE TABLE `fornecedor` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ENDERECO` varchar(255) DEFAULT NULL,
  `BAIRRO` varchar(60) DEFAULT NULL,
  `CIDADE` varchar(60) DEFAULT NULL,
  `UF` varchar(2) DEFAULT NULL,
  `CEP` varchar(15) DEFAULT NULL,
  `TELEFONE` varchar(20) DEFAULT NULL,
  `CELULAR` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(30) DEFAULT NULL,
  `CNPJ` varchar(20) DEFAULT NULL,
  `ATIVO` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Dumping data for table fornecedor
#

LOCK TABLES `fornecedor` WRITE;
/*!40000 ALTER TABLE `fornecedor` DISABLE KEYS */;
INSERT INTO `fornecedor` VALUES (1,1,'Produtos de Casa SA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'95348830000110',1);
/*!40000 ALTER TABLE `fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table grupo
#

DROP TABLE IF EXISTS `grupo`;
CREATE TABLE `grupo` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(11) NOT NULL,
  `DESCRICAO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=cp850;

#
# Dumping data for table grupo
#

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` VALUES (1,1,'Alimentos');
INSERT INTO `grupo` VALUES (2,2,'Bebidas');
INSERT INTO `grupo` VALUES (3,3,'Cama, Mesa e Banho');
INSERT INTO `grupo` VALUES (4,4,'Diversos');
INSERT INTO `grupo` VALUES (5,5,'Eletrodomésticos');
INSERT INTO `grupo` VALUES (6,6,'Eletrônicos');
INSERT INTO `grupo` VALUES (7,7,'Informática');
INSERT INTO `grupo` VALUES (8,8,'Móveis e Decoração');
INSERT INTO `grupo` VALUES (9,9,'Telefonia');
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table operacao
#

DROP TABLE IF EXISTS `operacao`;
CREATE TABLE `operacao` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CS_OPERACAO` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=cp850;

#
# Dumping data for table operacao
#

LOCK TABLES `operacao` WRITE;
/*!40000 ALTER TABLE `operacao` DISABLE KEYS */;
INSERT INTO `operacao` VALUES (1,'INCLUIR');
INSERT INTO `operacao` VALUES (2,'ALTERAR');
INSERT INTO `operacao` VALUES (3,'EXCLUIR');
INSERT INTO `operacao` VALUES (4,'PESQUISAR');
INSERT INTO `operacao` VALUES (5,'GERAR_RELATORIO');
INSERT INTO `operacao` VALUES (6,'VISUALIZACAO');
/*!40000 ALTER TABLE `operacao` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table produto
#

DROP TABLE IF EXISTS `produto`;
CREATE TABLE `produto` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `CODIGO_BARRAS` char(20) DEFAULT NULL,
  `ID_FORNECEDOR` int(11) NOT NULL,
  `ID_GRUPO` int(11) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `VALOR_CUSTO` double DEFAULT NULL,
  `VALOR_VENDA` double DEFAULT NULL,
  `VALOR_LUCRO` double DEFAULT NULL,
  `PERCENT_LUCRO` double DEFAULT NULL,
  `QTD_ESTOQUE` int(11) DEFAULT NULL,
  `QTD_ESTOQUE_MIN` int(11) DEFAULT NULL,
  `QTD_ESTOQUE_MAX` int(11) DEFAULT NULL,
  `LOCALIZACAO` varchar(255) DEFAULT NULL,
  `ATIVO` tinyint(1) DEFAULT NULL,
  `SRC_IMG` varchar(500) DEFAULT NULL,
  `ID_UNIDADE_MEDIDA_COMERCIAL` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`),
  KEY `FK_PRODUTO` (`ID_FORNECEDOR`),
  KEY `FK_GRUPO` (`ID_GRUPO`),
  KEY `FK_UNIDADE_MEDIDA_COMERCIAL` (`ID_UNIDADE_MEDIDA_COMERCIAL`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Dumping data for table produto
#

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,1,'1',1,9,'teste',2,2,0,0,2,2,2,'',1,NULL,61);
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table produto_codigo_barras
#

DROP TABLE IF EXISTS `produto_codigo_barras`;
CREATE TABLE `produto_codigo_barras` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_PRODUTO` int(11) DEFAULT NULL,
  `CODIGO_BARRAS` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRODUTO_CODIGO_BARRAS_ID_PRODUTO` (`ID_PRODUTO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Dumping data for table produto_codigo_barras
#

LOCK TABLES `produto_codigo_barras` WRITE;
/*!40000 ALTER TABLE `produto_codigo_barras` DISABLE KEYS */;
INSERT INTO `produto_codigo_barras` VALUES (1,1,'1');
INSERT INTO `produto_codigo_barras` VALUES (2,1,'2');
/*!40000 ALTER TABLE `produto_codigo_barras` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table programa
#

DROP TABLE IF EXISTS `programa`;
CREATE TABLE `programa` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(100) NOT NULL,
  `CS_TIPO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=cp850;

#
# Dumping data for table programa
#

LOCK TABLES `programa` WRITE;
/*!40000 ALTER TABLE `programa` DISABLE KEYS */;
INSERT INTO `programa` VALUES (1,'Cadastro de Cliente','Cadastro');
INSERT INTO `programa` VALUES (2,'Cadastro de Fornecedor','Cadastro');
INSERT INTO `programa` VALUES (3,'Cadastro de Grupo','Cadastro');
INSERT INTO `programa` VALUES (4,'Cadastro de Produto','Cadastro');
INSERT INTO `programa` VALUES (5,'Cadastro de Usuario','Cadastro');
INSERT INTO `programa` VALUES (6,'Realizar Venda','Cadastro');
INSERT INTO `programa` VALUES (7,'Cadastro de Empresa','Cadastro');
INSERT INTO `programa` VALUES (8,'Dashboard','Visualizacao');
/*!40000 ALTER TABLE `programa` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table programa_operacao
#

DROP TABLE IF EXISTS `programa_operacao`;
CREATE TABLE `programa_operacao` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_PROGRAMA` int(11) NOT NULL,
  `ID_OPERACAO` int(11) NOT NULL,
  `ID_USUARIO` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PROGRAMA_OPERACAO_ID_PROGRAMA` (`ID_PROGRAMA`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=cp850;

#
# Dumping data for table programa_operacao
#

LOCK TABLES `programa_operacao` WRITE;
/*!40000 ALTER TABLE `programa_operacao` DISABLE KEYS */;
INSERT INTO `programa_operacao` VALUES (1,1,1,1);
INSERT INTO `programa_operacao` VALUES (2,1,2,1);
INSERT INTO `programa_operacao` VALUES (3,1,3,1);
INSERT INTO `programa_operacao` VALUES (4,1,4,1);
INSERT INTO `programa_operacao` VALUES (5,2,1,1);
INSERT INTO `programa_operacao` VALUES (6,2,2,1);
INSERT INTO `programa_operacao` VALUES (7,2,3,1);
INSERT INTO `programa_operacao` VALUES (8,2,4,1);
INSERT INTO `programa_operacao` VALUES (9,3,1,1);
INSERT INTO `programa_operacao` VALUES (10,3,2,1);
INSERT INTO `programa_operacao` VALUES (11,3,3,1);
INSERT INTO `programa_operacao` VALUES (12,3,4,1);
INSERT INTO `programa_operacao` VALUES (13,4,1,1);
INSERT INTO `programa_operacao` VALUES (14,4,2,1);
INSERT INTO `programa_operacao` VALUES (15,4,3,1);
INSERT INTO `programa_operacao` VALUES (16,4,4,1);
INSERT INTO `programa_operacao` VALUES (17,5,1,1);
INSERT INTO `programa_operacao` VALUES (18,5,2,1);
INSERT INTO `programa_operacao` VALUES (19,5,3,1);
INSERT INTO `programa_operacao` VALUES (20,5,4,1);
INSERT INTO `programa_operacao` VALUES (21,6,1,1);
INSERT INTO `programa_operacao` VALUES (22,6,2,1);
INSERT INTO `programa_operacao` VALUES (23,6,3,1);
INSERT INTO `programa_operacao` VALUES (24,6,4,1);
INSERT INTO `programa_operacao` VALUES (25,7,1,1);
INSERT INTO `programa_operacao` VALUES (26,7,2,1);
INSERT INTO `programa_operacao` VALUES (27,7,3,1);
INSERT INTO `programa_operacao` VALUES (28,7,4,1);
INSERT INTO `programa_operacao` VALUES (29,8,6,1);
/*!40000 ALTER TABLE `programa_operacao` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table sistema_config
#

DROP TABLE IF EXISTS `sistema_config`;
CREATE TABLE `sistema_config` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(60) DEFAULT NULL,
  `SENHA` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=cp850;

#
# Dumping data for table sistema_config
#

LOCK TABLES `sistema_config` WRITE;
/*!40000 ALTER TABLE `sistema_config` DISABLE KEYS */;
INSERT INTO `sistema_config` VALUES (1,'rhuanpablo13@hotmail.com','JfvT/8V6tB5KSGvRu9lZ9Q==');
/*!40000 ALTER TABLE `sistema_config` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table unidade_medida_comercial
#

DROP TABLE IF EXISTS `unidade_medida_comercial`;
CREATE TABLE `unidade_medida_comercial` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(11) NOT NULL,
  `UNIDADE` varchar(25) NOT NULL,
  `DESCRICAO` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=cp850;

#
# Dumping data for table unidade_medida_comercial
#

LOCK TABLES `unidade_medida_comercial` WRITE;
/*!40000 ALTER TABLE `unidade_medida_comercial` DISABLE KEYS */;
INSERT INTO `unidade_medida_comercial` VALUES (1,1,'AMPOLA','AMPOLA');
INSERT INTO `unidade_medida_comercial` VALUES (2,2,'BALDE','BALDE');
INSERT INTO `unidade_medida_comercial` VALUES (3,3,'BANDEJ','BANDEJA');
INSERT INTO `unidade_medida_comercial` VALUES (4,4,'BARRA','BARRA');
INSERT INTO `unidade_medida_comercial` VALUES (5,5,'BISNAG','BISNAGA');
INSERT INTO `unidade_medida_comercial` VALUES (6,6,'BLOCO','BLOCO');
INSERT INTO `unidade_medida_comercial` VALUES (7,7,'BOBINA','BOBINA');
INSERT INTO `unidade_medida_comercial` VALUES (8,8,'BOMB','BOMBONA');
INSERT INTO `unidade_medida_comercial` VALUES (9,9,'CAPS','CAPSULA');
INSERT INTO `unidade_medida_comercial` VALUES (10,10,'CART','CARTELA');
INSERT INTO `unidade_medida_comercial` VALUES (11,11,'CENTO','CENTO');
INSERT INTO `unidade_medida_comercial` VALUES (12,12,'CJ','CONJUNTO');
INSERT INTO `unidade_medida_comercial` VALUES (13,13,'CM','CENTIMETRO');
INSERT INTO `unidade_medida_comercial` VALUES (14,14,'CM2','CENTIMETRO QUADRADO');
INSERT INTO `unidade_medida_comercial` VALUES (15,15,'CX','CAIXA');
INSERT INTO `unidade_medida_comercial` VALUES (16,16,'CX2','CAIXA COM 2 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (17,17,'CX3','CAIXA COM 3 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (18,18,'CX5','CAIXA COM 5 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (19,19,'CX10','CAIXA COM 10 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (20,20,'CX15','CAIXA COM 15 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (21,21,'CX20','CAIXA COM 20 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (22,22,'CX25','CAIXA COM 25 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (23,23,'CX50','CAIXA COM 50 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (24,24,'CX100','CAIXA COM 100 UNIDADES');
INSERT INTO `unidade_medida_comercial` VALUES (25,25,'DISP','DISPLAY');
INSERT INTO `unidade_medida_comercial` VALUES (26,26,'DUZIA','DUZIA');
INSERT INTO `unidade_medida_comercial` VALUES (27,27,'EMBAL','EMBALAGEM');
INSERT INTO `unidade_medida_comercial` VALUES (28,28,'FARDO','FARDO');
INSERT INTO `unidade_medida_comercial` VALUES (29,29,'FOLHA','FOLHA');
INSERT INTO `unidade_medida_comercial` VALUES (30,30,'FRASCO','FRASCO');
INSERT INTO `unidade_medida_comercial` VALUES (31,31,'GALAO','GALÃO');
INSERT INTO `unidade_medida_comercial` VALUES (32,32,'GF','GARRAFA');
INSERT INTO `unidade_medida_comercial` VALUES (33,33,'GRAMAS','GRAMAS');
INSERT INTO `unidade_medida_comercial` VALUES (34,34,'JOGO','JOGO');
INSERT INTO `unidade_medida_comercial` VALUES (35,35,'KG','QUILOGRAMA');
INSERT INTO `unidade_medida_comercial` VALUES (36,36,'KIT','KIT');
INSERT INTO `unidade_medida_comercial` VALUES (37,37,'LATA','LATA');
INSERT INTO `unidade_medida_comercial` VALUES (38,38,'LITRO','LITRO');
INSERT INTO `unidade_medida_comercial` VALUES (39,39,'M','METRO');
INSERT INTO `unidade_medida_comercial` VALUES (40,40,'M2','METRO QUADRADO');
INSERT INTO `unidade_medida_comercial` VALUES (41,41,'M3','METRO CÚBICO');
INSERT INTO `unidade_medida_comercial` VALUES (42,42,'MILHEI','MILHEIRO');
INSERT INTO `unidade_medida_comercial` VALUES (43,43,'ML','MILILITRO');
INSERT INTO `unidade_medida_comercial` VALUES (44,44,'MWH','MEGAWATT HORA');
INSERT INTO `unidade_medida_comercial` VALUES (45,45,'PACOTE','PACOTE');
INSERT INTO `unidade_medida_comercial` VALUES (46,46,'PALETE','PALETE');
INSERT INTO `unidade_medida_comercial` VALUES (47,47,'PARES','PARES');
INSERT INTO `unidade_medida_comercial` VALUES (48,48,'PC','PEÇA');
INSERT INTO `unidade_medida_comercial` VALUES (49,49,'POTE','POTE');
INSERT INTO `unidade_medida_comercial` VALUES (50,50,'K','QUILATE');
INSERT INTO `unidade_medida_comercial` VALUES (51,51,'RESMA','RESMA');
INSERT INTO `unidade_medida_comercial` VALUES (52,52,'ROLO','ROLO');
INSERT INTO `unidade_medida_comercial` VALUES (53,53,'SACO','SACO');
INSERT INTO `unidade_medida_comercial` VALUES (54,54,'SACOLA','SACOLA');
INSERT INTO `unidade_medida_comercial` VALUES (55,55,'TAMBOR','TAMBOR');
INSERT INTO `unidade_medida_comercial` VALUES (56,56,'TANQUE','TANQUE');
INSERT INTO `unidade_medida_comercial` VALUES (57,57,'TON','TONELADA');
INSERT INTO `unidade_medida_comercial` VALUES (58,58,'TUBO','TUBO');
INSERT INTO `unidade_medida_comercial` VALUES (59,59,'UNID','UNIDADE');
INSERT INTO `unidade_medida_comercial` VALUES (60,60,'VASIL','VASILHAME');
INSERT INTO `unidade_medida_comercial` VALUES (61,61,'VIDRO','VIDRO');
/*!40000 ALTER TABLE `unidade_medida_comercial` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table usuario
#

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `ATIVO` tinyint(1) DEFAULT NULL,
  `ADMINISTRADOR` tinyint(1) DEFAULT NULL,
  `CPF` varchar(20) DEFAULT NULL,
  `USUARIO` varchar(255) NOT NULL,
  `SENHA` varchar(40) NOT NULL,
  `NOME` varchar(255) NOT NULL,
  `EMAIL` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USUARIO` (`USUARIO`),
  UNIQUE KEY `CODIGO` (`CODIGO`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Dumping data for table usuario
#

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,1,1,'02533572179','rhuan','+R3q3yuttunKuRmqgThyCw==','Rhuan Pablo','rhuanpablo13@hotmail.com');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table venda
#

DROP TABLE IF EXISTS `venda`;
CREATE TABLE `venda` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODIGO` int(10) DEFAULT NULL,
  `DATA_VENDA` date DEFAULT NULL,
  `DESCONTO_PERCENT` float DEFAULT NULL,
  `DESCONTO_VALOR` float DEFAULT NULL,
  `TOTAL_PAGO` float DEFAULT NULL,
  `SUB_TOTAL` float DEFAULT NULL,
  `VALOR_TOTAL` float DEFAULT NULL,
  `ID_CLIENTE` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODIGO` (`CODIGO`),
  KEY `FK_VENDAS_1` (`ID_CLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table venda
#

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
/*!40000 ALTER TABLE `venda` ENABLE KEYS */;
UNLOCK TABLES;

#
#  Foreign keys for table carrinho_produto
#

ALTER TABLE `carrinho_produto`
ADD CONSTRAINT `FK_CARRINHO_PRODUTO_ID_PRODUTO` FOREIGN KEY (`ID_PRODUTO`) REFERENCES `produto` (`ID`),
ADD CONSTRAINT `FK_CARRINHO_PRODUTO_ID_CARRINHO` FOREIGN KEY (`ID_CARRINHO`) REFERENCES `carrinho` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

#
#  Foreign keys for table forma_pagamento
#

ALTER TABLE `forma_pagamento`
ADD CONSTRAINT `FK_FORMA_PAGAMENTO_ID_VENDA` FOREIGN KEY (`ID_VENDA`) REFERENCES `venda` (`ID`);

#
#  Foreign keys for table produto
#

ALTER TABLE `produto`
ADD CONSTRAINT `FK_PRODUTOS_FORNECEDOR` FOREIGN KEY (`ID_FORNECEDOR`) REFERENCES `fornecedor` (`ID`),
ADD CONSTRAINT `FK_PRODUTOS_GRUPO` FOREIGN KEY (`ID_GRUPO`) REFERENCES `grupo` (`ID`),
ADD CONSTRAINT `FK_PRODUTOS_UNIDADE_MEDIDA_COMERCIAL` FOREIGN KEY (`ID_UNIDADE_MEDIDA_COMERCIAL`) REFERENCES `unidade_medida_comercial` (`ID`);

#
#  Foreign keys for table programa_operacao
#

ALTER TABLE `programa_operacao`
ADD CONSTRAINT `FK_PROGRAMA_OPERACAO_ID_PROGRAMA` FOREIGN KEY (`ID_PROGRAMA`) REFERENCES `programa` (`ID`);

#
#  Foreign keys for table venda
#

ALTER TABLE `venda`
ADD CONSTRAINT `FK_VENDA_CLIENTE` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`ID`);


/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
