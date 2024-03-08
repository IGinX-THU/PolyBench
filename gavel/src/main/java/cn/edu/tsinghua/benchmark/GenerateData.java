package cn.edu.tsinghua.benchmark;

import cn.edu.tsinghua.benchmark.item.*;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GenerateData {

    private static final int NUMBER_OF_CATEGORIES = 35;

//    private static final int NUMBER_OF_USERS = 1_500;75_000;
    private static final int NUMBER_OF_USERS = 1_500;

//    private static final int NUMBER_OF_AUCTIONS = 750;37_000;
    private static final int NUMBER_OF_AUCTIONS = 750;

    private static final int MIN_LENGTH_OF_AN_AUCTION_TITLE = 2 * 6;

    private static final int MAX_LENGTH_OF_AN_AUCTION_TITLE = 8 * 6;

    private static final int MIN_LENGTH_OF_AUCTION_DESCRIPTION = 5 * 6 * 8;

    private static final int MAX_LENGTH_OF_AUCTION_DESCRIPTION = 40 * 6 * 8;

    private static final int MIN_NUMBER_OF_BIDS_PER_AUCTION = 30;

    private static final int MAX_NUMBER_OF_BIDS_PER_AUCTION = 200;

    private static final int MIN_PICTURES_PER_AUCTION = 1;

    private static final int MAX_PICTURES_PER_AUCTION = 6;

    private static final int DURATION_OF_AN_AUCTION = 10;

    private static final int MAX_AGE_OF_AN_AUCTION = 4;

    private static final Faker FAKER = new Faker();

    private static final Random RANDOM = new Random();

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static List<Item> auctionList = new ArrayList<>();

    private static List<Item> bidList = new ArrayList<>();

    private static List<Item> categoryList = new ArrayList<>();

    private static List<Item> pictureList = new ArrayList<>();

    private static List<Item> userList = new ArrayList<>();

    public static void main(String[] args) {
        generateTables();
        exportTables();
    }

    private static void generateTables() {
        long timeCnt = 1;
        // 生成user
        for (int i = 1; i <= NUMBER_OF_USERS; i++) {
            int userId = i;
            String email = FAKER.internet().emailAddress();
            String password = RandomStringUtils.randomAlphanumeric(10);
            String lastName = FAKER.name().lastName();
            String firstName = FAKER.name().firstName();
            String gender = RANDOM.nextBoolean() ? "male" : "female";
            String birthday = FORMAT.format(FAKER.date().birthday(16, 100));
            Address address = FAKER.address();
            String country = address.country();
            String city = address.city();
            String zipCode = address.zipCode();

            userList.add(new User(userId, email, password, lastName, firstName, gender, birthday, country, city, zipCode));

            timeCnt++;
        }

        // 生成category
        for (int i = 1; i <= NUMBER_OF_CATEGORIES; i++) {
            int categoryId = i;
            String name = FAKER.artist().name();

            categoryList.add(new Category(categoryId, name));

            timeCnt++;
        }

        int bidCnt = 1;
        // 生成auction
        for (int i = 1; i <= NUMBER_OF_AUCTIONS; i++) {
            int auctionId = i;
            String title = RandomStringUtils.randomAlphabetic(MIN_LENGTH_OF_AN_AUCTION_TITLE, MAX_LENGTH_OF_AN_AUCTION_TITLE);
            String description = RandomStringUtils.randomAlphabetic(MIN_LENGTH_OF_AUCTION_DESCRIPTION, MAX_LENGTH_OF_AUCTION_DESCRIPTION);
            Date startDate = FAKER.date().past(MAX_AGE_OF_AN_AUCTION * 365, 20, TimeUnit.DAYS);
            Date endDate = new Date(startDate.getTime() + (DURATION_OF_AN_AUCTION * 1000 * 60 * 60 * 24));
            long categoryId = 1L + RANDOM.nextInt(NUMBER_OF_CATEGORIES);
            long userId = 1L + RANDOM.nextInt(NUMBER_OF_USERS);

            auctionList.add(new Auction(timeCnt, auctionId, title, description, FORMAT.format(startDate), FORMAT.format(endDate), categoryId, userId));

            timeCnt++;

            // 生成picture
            for (int j = MIN_PICTURES_PER_AUCTION; j <= MIN_PICTURES_PER_AUCTION + RANDOM.nextInt(MAX_PICTURES_PER_AUCTION - MIN_PICTURES_PER_AUCTION); j++) {
                String filename = FAKER.file().fileName();
                int type = RANDOM.nextInt(10);
                long size = Math.abs(RANDOM.nextLong());

                pictureList.add(new Picture(filename, type, size, auctionId));

                timeCnt++;
            }

            // 生成bid
            int baseAmount = RANDOM.nextInt(100000);
            Date baseDate = startDate;
            int maxNumberOfBids = MIN_NUMBER_OF_BIDS_PER_AUCTION + RANDOM.nextInt(MAX_NUMBER_OF_BIDS_PER_AUCTION - MIN_NUMBER_OF_BIDS_PER_AUCTION);
            for (int j = 1; j <= maxNumberOfBids; j++) {
                int bidId = bidCnt;
                baseAmount += RANDOM.nextInt(10000);
                int amount = baseAmount;
                baseDate = FAKER.date().between(baseDate, new Date(baseDate.getTime() + (endDate.getTime() - baseDate.getTime()) / maxNumberOfBids));
                long timestamp = baseDate.getTime();
                long bidUserId;
                if (j == maxNumberOfBids) {
                    bidUserId = userId;
                } else {
                    bidUserId = RANDOM.nextInt(NUMBER_OF_USERS);
                }

                bidList.add(new Bid(bidId, amount, timestamp, bidUserId, auctionId));

                bidCnt++;
                timeCnt++;
            }
        }
    }

    private static void exportTables() {
        exportSingleTable("user", userList, "id", "email", "password", "last_name", "first_name", "gender", "birthday", "country", "city", "zip_code");
        exportSingleTable("picture", pictureList, "filename", "type", "size", "auction");
        exportSingleTable("category", categoryList, "id", "name");
        exportSingleTable("bid", bidList, "Time", "root.gavel.bid.id(INT32)", "root.gavel.bid.amount(INT32)", "root.gavel.bid.user(INT32)", "root.gavel.bid.auction(INT32)");
        exportSingleTable("auction", auctionList, "Time", "gavel.auction.id", "gavel.auction.title", "gavel.auction.description", "gavel.auction.start_date", "gavel.auction.end_date", "gavel.auction.category", "gavel.auction.user");
    }

    private static void exportSingleTable(String tableName, List<Item> table, String... header) {
        try {
            CSVPrinter printer = CSVFormat.Builder
                .create(CSVFormat.DEFAULT)
                .setHeader(header)
                .build()
                .print(new PrintWriter("gavel/generatedData/" + tableName + ".csv"));

            printer.printRecords(table.stream().map(Item::collectAttributes).collect(Collectors.toList()));

            printer.flush();
            printer.close();
        } catch (IOException e) {
            System.err.printf(
                "[ERROR] Encounter an error when writing csv file [%s].csv, because %s%n",
                tableName, e.getMessage());
        }
    }

}
