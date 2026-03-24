package vn.edu.phoneshop.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import vn.edu.phoneshop.dao.CustomerDAO;

@WebListener
public class NewYearPointResetListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleNextReset();
        System.out.println("[NewYearPointResetListener] Đã khởi động luồng ngầm: Tự động reset điểm tích lũy vào 00:00 ngày 1/1.");
    }

    private void scheduleNextReset() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime nextJan1st = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        // Nếu hiện thời gian hiện tại đã qua (hoặc đúng bằng) 00:00 ngày 1/1 của năm nay, ta hẹn qua ngày 1/1 năm sau
        if (now.compareTo(nextJan1st) >= 0) {
            nextJan1st = nextJan1st.plusYears(1);
        }
        
        long initialDelay = Duration.between(now, nextJan1st).toMillis();
        
        scheduler.schedule(() -> {
            try {
                CustomerDAO dao = new CustomerDAO();
                dao.resetPointsNewYear();
                System.out.println("[NewYearPointResetListener] Đã chạy lệnh reset điểm thưởng cho năm mới lúc: " + LocalDateTime.now(ZoneId.systemDefault()));
            } catch (Exception e) {
                System.err.println("[NewYearPointResetListener] Lỗi khi reset điểm: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Sau khi chạy xong, tiếp tục setup lịch hẹn cho năm kế tiếp
                scheduleNextReset();
            }
        }, initialDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("[NewYearPointResetListener] Đã tắt luồng ngầm reset điểm.");
        }
    }
}
