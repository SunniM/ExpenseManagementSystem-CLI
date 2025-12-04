package com.revature.expensemanager.service;

import java.sql.Connection;
import java.util.Calendar;

import com.revature.expensemanager.JDBC.ApprovalJDBC;
import com.revature.expensemanager.model.Approval;

public class ApprovalService {
    private ApprovalJDBC approvalJDBC;

    public ApprovalService(Connection connection) {
        approvalJDBC = new ApprovalJDBC(connection);
    }

    private void updateStatus(int expenseID, int reviewerID, String comment, String status) {
        Approval approval = approvalJDBC.getByExpenseID(expenseID);

        if (approval.getStatus().equals("PENDING")) {
            approval.setStatus(status);
            approval.setReviewer(reviewerID);
            Calendar now = Calendar.getInstance();
            approval.setReviewDate(
                    now.get(Calendar.YEAR) + "-"
                            + (now.get(Calendar.MONTH) + 1) + "-"
                            + now.get(Calendar.DAY_OF_MONTH));
            approval.setComment(comment);
        }
        approvalJDBC.update(approval);
    }

    public void approveExpense(int expenseID, int reviewerID, String comment) {
        updateStatus(expenseID, reviewerID, comment, "APPROVED");
    }

    public void denyExpense(int expenseID, int reviewerID, String comment) {
        updateStatus(expenseID, reviewerID, comment, "DENIED");
    }
}
