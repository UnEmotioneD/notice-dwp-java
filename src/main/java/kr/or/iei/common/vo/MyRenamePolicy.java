package kr.or.iei.common.vo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyRenamePolicy implements FileRenamePolicy {

	@Override
	public File rename(File originFile) {

		// 현재 시간
		long currentTime = System.currentTimeMillis();

		// ms 단위까지 겹칠 수 있어서 뒤에 랜덤 값을 붙여줌

		int ranNum = new Random().nextInt(10000) + 1; // 1 부터 10000 까지 랜던한 숫자

		String str = "_" + String.format("%05d", ranNum); // 랜덤 숫자를 모두 5자리로 생성

		String name = originFile.getName();
		String ext = null;

		int dot = name.lastIndexOf("."); // 파일명.확장자 에서 . 의 위치

		if (dot != -1) {
			// 파일명에 . 이 있을때
			ext = name.substring(dot); // 확장자
		} else {
			// 파일명에 . 이 없을때
			ext = "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		String fileName = sdf.format(new Date(currentTime)) + str + ext;

		File newFile = new File(originFile.getParent(), fileName);

		return newFile;
	}

}
