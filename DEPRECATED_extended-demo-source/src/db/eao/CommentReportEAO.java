package db.eao;

import javax.ejb.Stateless;

import db.eao.local.CommentReportEAOLocal;
import db.entity.CommentReport;

@Stateless
public class CommentReportEAO extends BaseEAO<CommentReport> implements CommentReportEAOLocal {

}
