package com.bitacademy.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitacademy.mysite.repository.BoardRepository;
import com.bitacademy.mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardRepository br;
	
	public List<BoardVo> getBoardList(int selected) {
		return br.findAllByRange(selected);
	}

	public BoardVo getBoardByNo(Long no) {
		return br.findByNo(no);
	}

	public void write(Long no, BoardVo vo) {
		BoardVo parentVo = null;
		if(no != null) {
			parentVo = br.findByNo(no);
		}
		br.insert(parentVo, vo);
	}

	public void updateBoard(BoardVo vo) {
		br.update(vo);
	}

	public void delete(Long no) {
		br.deleteByNo(no);
	}

	public int amount() {
		return br.length();
	}
	
}
