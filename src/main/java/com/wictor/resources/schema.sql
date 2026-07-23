-- --------------------------------------------------------
-- Servidor:                     127.0.0.1
-- Versão do servidor:           8.0.30 - MySQL Community Server - GPL
-- OS do Servidor:               Win64
-- HeidiSQL Versão:              12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Copiando estrutura do banco de dados para academia
CREATE DATABASE IF NOT EXISTS `academia` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `academia`;

-- Copiando estrutura para tabela academia.aluno
CREATE TABLE IF NOT EXISTS `aluno` (
  `Aluno_id` int NOT NULL,
  `Aluno_saude` text NOT NULL,
  `Aluno_obs` text,
  `Aluno_altura` decimal(3,2) NOT NULL,
  `Aluno_peso` decimal(5,2) NOT NULL,
  `Aluno_obj` enum('FORCA','HIPERTROFIA','RESISTENCIA','FUNCIONAL','EMAGRECIMENTO','MOBILIDADE','REABILITACAO') NOT NULL,
  `Aluno_plano` int DEFAULT NULL,
  `Aluno_vencimento` date NOT NULL,
  `Aluno_plano_vencido` tinyint(1) NOT NULL,
  `Aluno_abacate_customer_id` varchar(255) DEFAULT NULL,
  `Aluno_assinatura_ativa` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Aluno_id`),
  KEY `Aluno_plano` (`Aluno_plano`),
  CONSTRAINT `aluno_ibfk_1` FOREIGN KEY (`Aluno_id`) REFERENCES `user` (`User_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `aluno_ibfk_2` FOREIGN KEY (`Aluno_plano`) REFERENCES `plano` (`Plano_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.aluno: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `Catg_id` int NOT NULL AUTO_INCREMENT,
  `Catg_nome` varchar(255) NOT NULL,
  `Catg_sal` decimal(7,2) NOT NULL,
  `Catg_role` enum('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO') NOT NULL,
  PRIMARY KEY (`Catg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.categoria: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.exercicio
CREATE TABLE IF NOT EXISTS `exercicio` (
  `Exerc_id` int NOT NULL AUTO_INCREMENT,
  `Exerc_nome` varchar(255) NOT NULL,
  `Exerc_maq` int DEFAULT NULL,
  `Exerc_obs` text,
  `Exerc_foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Exerc_id`),
  KEY `Exerc_maq` (`Exerc_maq`),
  CONSTRAINT `exercicio_ibfk_1` FOREIGN KEY (`Exerc_maq`) REFERENCES `maquina` (`Maq_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.exercicio: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.financeiro
CREATE TABLE IF NOT EXISTS `financeiro` (
  `Fin_id` int NOT NULL AUTO_INCREMENT,
  `Fin_nome` varchar(255) NOT NULL,
  `Fin_tipo` enum('VENDA','MENSALIDADE','PAGAMENTO','RECEITA_EXTRA','DESPESA','SALARIO','MANUTENCAO') NOT NULL,
  `Fin_data` date NOT NULL,
  `Fin_val` decimal(10,2) NOT NULL,
  `Fin_origem` enum('PAGAMENTO_PRESENCIAL','PAGAMENTO_ONLINE','RENOVACAO_ASSINATURA','VENDA_PRODUTO','LANCAMENTO_MANUAL','AUTOMATICO') NOT NULL,
  `Func_id` int DEFAULT NULL,
  `Pag_id` int DEFAULT NULL,
  PRIMARY KEY (`Fin_id`),
  KEY `Func_id` (`Func_id`),
  KEY `Pag_id` (`Pag_id`),
  CONSTRAINT `financeiro_ibfk_1` FOREIGN KEY (`Func_id`) REFERENCES `funcionario` (`Func_id`) ON DELETE RESTRICT,
  CONSTRAINT `financeiro_ibfk_2` FOREIGN KEY (`Pag_id`) REFERENCES `pagamento` (`Pag_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.financeiro: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.funcionario
CREATE TABLE IF NOT EXISTS `funcionario` (
  `Func_id` int NOT NULL,
  `Func_cref` varchar(11) DEFAULT NULL,
  `Func_tipo` enum('CLT','PJ') NOT NULL,
  `Func_turno_ini` time NOT NULL,
  `Func_turno_fim` time NOT NULL,
  `Func_banco` varchar(255) NOT NULL,
  `Func_agencia` varchar(255) NOT NULL,
  `Func_conta` varchar(255) NOT NULL,
  `Func_tipo_conta` enum('CORRENTE','POUPANCA','SALARIO','PAGAMENTO','CONJUNTA','INVESTIMENTO') NOT NULL,
  `Catg_id` int NOT NULL,
  PRIMARY KEY (`Func_id`),
  UNIQUE KEY `Func_cref` (`Func_cref`),
  KEY `Catg_id` (`Catg_id`),
  CONSTRAINT `funcionario_ibfk_1` FOREIGN KEY (`Func_id`) REFERENCES `user` (`User_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `funcionario_ibfk_2` FOREIGN KEY (`Catg_id`) REFERENCES `categoria` (`Catg_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.funcionario: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.item_financeiro
CREATE TABLE IF NOT EXISTS `item_financeiro` (
  `Fin_id` int NOT NULL,
  `Prod_id` int NOT NULL,
  `ItemFin_qtd` int NOT NULL,
  `ItemFin_val_unit` decimal(10,2) NOT NULL,
  `ItemFin_desconto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`Fin_id`,`Prod_id`),
  KEY `Prod_id` (`Prod_id`),
  CONSTRAINT `item_financeiro_ibfk_1` FOREIGN KEY (`Fin_id`) REFERENCES `financeiro` (`Fin_id`) ON DELETE CASCADE,
  CONSTRAINT `item_financeiro_ibfk_2` FOREIGN KEY (`Prod_id`) REFERENCES `produto` (`Prod_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.item_financeiro: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.log
CREATE TABLE IF NOT EXISTS `log` (
  `Log_id` int NOT NULL AUTO_INCREMENT,
  `Log_acao` enum('CADASTRO','ALTERACAO','EXCLUSAO','CONSULTA','LOGIN','LOGOUT','VENDA','RELATORIO','OUTRO') NOT NULL,
  `Log_entidade` varchar(255) NOT NULL,
  `Log_entidade_id` int DEFAULT NULL,
  `Log_descricao` text NOT NULL,
  `Log_sucesso` tinyint(1) NOT NULL,
  `Log_metodo` varchar(10) NOT NULL,
  `Log_url` varchar(255) NOT NULL,
  `Log_ip` varchar(45) NOT NULL,
  `Log_data` datetime NOT NULL,
  `User_id` int DEFAULT NULL,
  PRIMARY KEY (`Log_id`),
  KEY `User_id` (`User_id`),
  CONSTRAINT `log_ibfk_1` FOREIGN KEY (`User_id`) REFERENCES `user` (`User_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.log: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.maquina
CREATE TABLE IF NOT EXISTS `maquina` (
  `Maq_id` int NOT NULL AUTO_INCREMENT,
  `Maq_nome` varchar(255) NOT NULL,
  `Maq_ativa` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`Maq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.maquina: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.pagamento
CREATE TABLE IF NOT EXISTS `pagamento` (
  `Pag_id` int NOT NULL AUTO_INCREMENT,
  `Pag_abacate_bill_id` varchar(255) NOT NULL,
  `Pag_abacate_subscription_id` varchar(255) DEFAULT NULL,
  `Pag_external_id` varchar(255) DEFAULT NULL,
  `Pag_data` datetime NOT NULL,
  `Pag_forma` enum('PIX','CARTAO','DINHEIRO') NOT NULL,
  `Pag_status` enum('PENDENTE','PAGO','CANCELADO','FALHOU') NOT NULL,
  `Aluno_id` int NOT NULL,
  `Plano_id` int NOT NULL,
  PRIMARY KEY (`Pag_id`),
  UNIQUE KEY `Pag_external_id` (`Pag_external_id`),
  KEY `Aluno_id` (`Aluno_id`),
  KEY `Plano_id` (`Plano_id`),
  CONSTRAINT `pagamento_ibfk_1` FOREIGN KEY (`Aluno_id`) REFERENCES `aluno` (`Aluno_id`) ON DELETE RESTRICT,
  CONSTRAINT `pagamento_ibfk_2` FOREIGN KEY (`Plano_id`) REFERENCES `plano` (`Plano_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.pagamento: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.plano
CREATE TABLE IF NOT EXISTS `plano` (
  `Plano_id` int NOT NULL AUTO_INCREMENT,
  `Plano_nome` varchar(255) NOT NULL,
  `Plano_valor` decimal(5,2) NOT NULL,
  `Plano_ciclo` enum('AVULSO','MENSAL','SEMESTRAL','ANUAL') NOT NULL,
  `Plano_abacate_id` varchar(255) DEFAULT NULL,
  `Plano_recorrente` tinyint(1) NOT NULL,
  `Plano_ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Plano_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.plano: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.produto
CREATE TABLE IF NOT EXISTS `produto` (
  `Prod_id` int NOT NULL AUTO_INCREMENT,
  `Prod_nome` varchar(255) NOT NULL,
  `Prod_desc` varchar(255) DEFAULT NULL,
  `Prod_preco` decimal(10,2) NOT NULL,
  `Prod_qtd` int NOT NULL,
  `Prod_qtd_min` int NOT NULL,
  `Prod_foto` varchar(255) DEFAULT NULL,
  `Prod_ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Prod_id`),
  UNIQUE KEY `Prod_nome` (`Prod_nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.produto: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.treino
CREATE TABLE IF NOT EXISTS `treino` (
  `Treino_id` int NOT NULL AUTO_INCREMENT,
  `Treino_nome` varchar(255) NOT NULL,
  `Treino_obj` enum('FORCA','HIPERTROFIA','RESISTENCIA','FUNCIONAL','EMAGRECIMENTO','MOBILIDADE','REABILITACAO') NOT NULL,
  `Treino_inicio` date NOT NULL,
  `Treino_fim` date NOT NULL,
  `Treino_criado` date NOT NULL,
  `Treino_modificado` date NOT NULL,
  `Treino_obs` varchar(255) DEFAULT NULL,
  `Treino_ativo` tinyint(1) NOT NULL,
  `Aluno_id` int DEFAULT NULL,
  PRIMARY KEY (`Treino_id`),
  KEY `Aluno_id` (`Aluno_id`),
  CONSTRAINT `treino_ibfk_1` FOREIGN KEY (`Aluno_id`) REFERENCES `aluno` (`Aluno_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.treino: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.treino_exercicio
CREATE TABLE IF NOT EXISTS `treino_exercicio` (
  `Treino_id` int NOT NULL,
  `Exerc_id` int NOT NULL,
  `ExercT_ordem` int NOT NULL,
  `ExercT_carga` varchar(255) NOT NULL,
  `ExercT_series` varchar(255) NOT NULL,
  `ExercT_rep` varchar(255) NOT NULL,
  `ExercT_obs` text,
  PRIMARY KEY (`Treino_id`,`Exerc_id`),
  KEY `Exerc_id` (`Exerc_id`),
  CONSTRAINT `treino_exercicio_ibfk_1` FOREIGN KEY (`Treino_id`) REFERENCES `treino` (`Treino_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `treino_exercicio_ibfk_2` FOREIGN KEY (`Exerc_id`) REFERENCES `exercicio` (`Exerc_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.treino_exercicio: ~0 rows (aproximadamente)

-- Copiando estrutura para tabela academia.user
CREATE TABLE IF NOT EXISTS `user` (
  `User_id` int NOT NULL AUTO_INCREMENT,
  `User_cpf` varchar(255) NOT NULL,
  `User_senha` varchar(255) NOT NULL,
  `User_nome` varchar(255) NOT NULL,
  `User_email1` varchar(255) NOT NULL,
  `User_email2` varchar(255) DEFAULT NULL,
  `User_tel1` char(11) NOT NULL,
  `User_tel2` char(11) DEFAULT NULL,
  `User_sexo` enum('M','F') NOT NULL,
  `User_cep` char(8) NOT NULL,
  `User_bairro` varchar(255) NOT NULL,
  `User_rua` varchar(255) NOT NULL,
  `User_numcasa` int NOT NULL,
  `User_comp` varchar(255) DEFAULT NULL,
  `User_foto` varchar(255) DEFAULT NULL,
  `User_datanasc` date NOT NULL,
  `User_role` enum('ADMIN','GERENTE','RECEPCIONISTA','PROFESSOR','ALUNO') NOT NULL,
  `User_ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`User_id`),
  UNIQUE KEY `User_cpf` (`User_cpf`),
  UNIQUE KEY `User_email1` (`User_email1`),
  UNIQUE KEY `User_tel1` (`User_tel1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Copiando dados para a tabela academia.user: ~0 rows (aproximadamente)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
