package lotto;

import camp.nextstep.edu.missionutils.Console;

public class LottoGame {
    public void execute() {
        int inputPurchaseMoney = purchase();
    }

    private int purchase() {
        while (true) {
            System.out.println("구입금액을 입력해 주세요.");
            int inputPurchaseMoney;
            try {
                inputPurchaseMoney = Integer.parseInt(Console.readLine());
                validatePurchaseMoney(inputPurchaseMoney);
                return inputPurchaseMoney;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validatePurchaseMoney(int inputPurchaseMoney) {
        if (inputPurchaseMoney % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 1,000원 단위로 입력해 주세요.");
        }
    }
}
