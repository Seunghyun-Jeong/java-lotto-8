package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.List;

public class LottoGame {
    public void execute() {
        int inputPurchaseMoney = purchase();
        int purchaseLottoCount = getPurchaseLottoCount(inputPurchaseMoney);
        List<Lotto> IssueLottoNumbers = getPurchaseLotto(purchaseLottoCount);
    }

    private int purchase() {
        while (true) {
            System.out.println("구입금액을 입력해 주세요.");
            int inputPurchaseMoney;
            try {
                inputPurchaseMoney = Integer.parseInt(Console.readLine());
                validatePurchaseMoney(inputPurchaseMoney);
                System.out.println();
                return inputPurchaseMoney;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int getPurchaseLottoCount(int inputPurchaseMoney) {
        int purchaseLottoCount = (inputPurchaseMoney / 1000);
        System.out.println(purchaseLottoCount + "개를 구매했습니다.");
        return purchaseLottoCount;
    }

    private List<Lotto> getPurchaseLotto(int purchaseLottoCount) {
        List<Lotto> purchaseLotto = new ArrayList<>();
        for (int i = 0; i < purchaseLottoCount; i++) {
            List<Integer> lottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6).stream().sorted().toList();
            purchaseLotto.add(new Lotto(lottoNumbers));
        }

        for (Lotto lotto : purchaseLotto) {
            lotto.printLottoNumbers();
        }

        return purchaseLotto;
    }

    private void validatePurchaseMoney(int inputPurchaseMoney) {
        if (inputPurchaseMoney % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 1,000원 단위로 입력해 주세요.");
        }
    }
}
