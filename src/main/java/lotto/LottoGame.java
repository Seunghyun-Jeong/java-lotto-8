package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LottoGame {
    public void execute() {
        int purchaseMoney = inputPurchaseMoney();
        int purchaseLottoCount = getPurchaseLottoCount(purchaseMoney);
        List<Lotto> issueLottoNumbers = getPurchaseLotto(purchaseLottoCount);
        Lotto winningLotto = inputWinningLottoNumbers();
        int bonusNumber = inputBonusNumber(winningLotto);
        matchWinning(winningLotto, issueLottoNumbers, bonusNumber, purchaseMoney);
    }

    private int inputPurchaseMoney() {
        while (true) {
            System.out.println("구입금액을 입력해 주세요.");
            int purchaseMoney;
            try {
                purchaseMoney = Integer.parseInt(Console.readLine());
                validatePurchaseMoney(purchaseMoney);
                System.out.println();
                return purchaseMoney;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int getPurchaseLottoCount(int purchaseMoney) {
        int purchaseLottoCount = (purchaseMoney / 1000);
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
        System.out.println();

        return purchaseLotto;
    }

    private Lotto inputWinningLottoNumbers() {
        while (true) {
            System.out.println("당첨 번호를 입력해 주세요.");
            try {
                List<Integer> winningNumbers = Arrays.stream(Console.readLine().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println();
                return new Lotto(winningNumbers);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int inputBonusNumber(Lotto winningLotto) {
        while (true) {
            System.out.println("보너스 번호를 입력해 주세요.");
            try {
                int bonusNumber = Integer.parseInt(Console.readLine());
                validateBonusNumber(bonusNumber);
                System.out.println();
                return bonusNumber;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void matchWinning(Lotto winningLotto, List<Lotto> issueLottoNumbers, int bonusNumber, int purchaseMoney) {
        HashMap<WinningLottoType, Integer> winningResultMap = matchWinningResult(winningLotto, issueLottoNumbers, bonusNumber);
        printWinningResult(winningResultMap, purchaseMoney);
    }

    private void printWinningResult(HashMap<WinningLottoType, Integer> winningResultMap, int purchaseMoney) {
        int winningReward = 0;
        System.out.println("당첨 통계");
        System.out.println("---");
        for (WinningLottoType winningLottoType : WinningLottoType.values()) {
            int winningCount = winningResultMap.get(winningLottoType);
            System.out.println(winningLottoType.getComment() + " - " + winningCount + "개");
            winningReward += winningLottoType.getReward() * winningCount;
        }

        printRewardRate(purchaseMoney, winningReward);
    }

    private void printRewardRate(int purchaseMoney, int reward) {
        System.out.println("총 수익률은 "
                + getRewardRate((double) purchaseMoney, (double) reward)
                + "%입니다.");
    }

    protected String getRewardRate(double purchaseMoney, double reward) {
        return new DecimalFormat("0.0").format(reward * 100.0 / purchaseMoney);
    }

    private HashMap<WinningLottoType, Integer> matchWinningResult(Lotto winningLotto, List<Lotto> purchaseLottos,
                                                                    int bonusNumber) {
        HashMap<WinningLottoType, Integer> winningResultMap = makeWinningResultMap();
        for (Lotto purchaseLotto : purchaseLottos) {
            int matchCount = winningLotto.matchCount(purchaseLotto);
            boolean matchBonus = purchaseLotto.containBounsNumber(bonusNumber);

            WinningLottoType winningLottoType = WinningLottoType.getWinningLottoTypeByMatch(matchCount, matchBonus);
            if (winningLottoType != null) {
                int winningTypeCount = winningResultMap.get(winningLottoType);
                winningResultMap.put(winningLottoType, winningTypeCount + 1);
            }
        }
        return winningResultMap;
    }

    private HashMap<WinningLottoType, Integer> makeWinningResultMap() {
        HashMap<WinningLottoType, Integer> winningResultMap = new HashMap<>();
        for (WinningLottoType winningLottoType : WinningLottoType.values()) {
            winningResultMap.put(winningLottoType, 0);
        }
        return winningResultMap;
    }


    private void validatePurchaseMoney(int purchaseMoney) {
        if (purchaseMoney % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 1,000원 단위로 입력해 주세요.");
        }
    }

    private void validateBonusNumber(int bonusNumber) {
        if (bonusNumber < 1 || bonusNumber > 45) {
            throw new IllegalArgumentException("[ERROR] 번호는 1부터 45 사이의 숫자만 입력해 주세요.");
        }
    }
}
