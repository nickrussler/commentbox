package db.eao;

import javax.ejb.Stateless;

import db.eao.local.ArticleReportEAOLocal;
import db.entity.ArticleReport;

@Stateless
public class ArticleReportEAO extends BaseEAO<ArticleReport> implements ArticleReportEAOLocal {

}
