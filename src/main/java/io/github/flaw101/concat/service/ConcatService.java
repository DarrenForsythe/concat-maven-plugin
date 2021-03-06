/**
 * MIT License
 *
 * Copyright (c) 2018 Darren Forsythe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.flaw101.concat.service;

import com.google.inject.Inject;

import io.github.flaw101.concat.ConcatParams;
import io.github.flaw101.concat.filewriter.FileWriterService;
import io.github.flaw101.concat.validate.ValidationFailedException;
import io.github.flaw101.concat.validate.ValidatorService;

/**
 * Main service to handle the concat.
 *
 * @author Darren Forsythe
 * @since 1.1.2
 *
 */
public class ConcatService {

	private final ValidatorService validatorService;
	private final FileWriterService fileWriterService;

	@Inject
	public ConcatService(final ValidatorService validatorService,
			final FileWriterService fileWriterService) {
		this.validatorService = validatorService;
		this.fileWriterService = fileWriterService;
	}

	public void concat(final ConcatParams params) throws ValidationFailedException {
		validatorService.validate(params);
		fileWriterService.writeToOutputfile(params);
	}
}
