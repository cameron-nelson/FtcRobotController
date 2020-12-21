/*
 Copyright (c) 2020 HF Robotics (http://www.hfrobots.com)
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package com.hfrobots.tnt.season2021;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import lombok.Getter;

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP.
*
* @author GRIP
*/
public class GripPipeline {

	//Outputs
	private Mat hsvThresholdOutput = new Mat();
	private Mat cvCvtcolorOutput = new Mat();
	private Mat cvExtractchannelOutput = new Mat();
	private Mat blurOutput = new Mat();

	@Getter
	private Mat cvAddOutput = new Mat();

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process(Mat source0) {
		// Step HSV_Threshold0:
		Mat hsvThresholdInput = source0;
		double[] hsvThresholdHue = {0.0, 17.346568110980726};
		double[] hsvThresholdSaturation = {107.77877697841726, 255.0};
		double[] hsvThresholdValue = {123.83093525179855, 255.0};
		hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

		// Step CV_cvtColor0:
		Mat cvCvtcolorSrc = source0;
		int cvCvtcolorCode = Imgproc.COLOR_RGB2YCrCb;
		cvCvtcolor(cvCvtcolorSrc, cvCvtcolorCode, cvCvtcolorOutput);

		// Step CV_extractChannel0:
		Mat cvExtractchannelSrc = cvCvtcolorOutput;
		double cvExtractchannelChannel = 2.0;
		cvExtractchannel(cvExtractchannelSrc, cvExtractchannelChannel, cvExtractchannelOutput);

		// Step Blur0:
		Mat blurInput = hsvThresholdOutput;
		BlurType blurType = BlurType.get("Box Blur");
		double blurRadius = 21.69811320754717;
		blur(blurInput, blurType, blurRadius, blurOutput);

		// Step CV_add0:
		Mat cvAddSrc1 = cvExtractchannelOutput;
		Mat cvAddSrc2 = blurOutput;
		cvAdd(cvAddSrc1, cvAddSrc2, cvAddOutput);
	}

	/**
	 * Segment an image based on hue, saturation, and value ranges.
	 *
	 * @param input The image on which to perform the HSL threshold.
	 * @param hue The min and max hue
	 * @param sat The min and max saturation
	 * @param val The min and max value
	 * @param out The image in which to store the output.
	 */
	private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
	    Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
		Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
			new Scalar(hue[1], sat[1], val[1]), out);
	}

	/**
	 * Converts an image from one color space to another.
	 * @param src Image to convert.
	 * @param code conversion code.
	 * @param dst converted Image.
	 */
	private void cvCvtcolor(Mat src, int code, Mat dst) {
		Imgproc.cvtColor(src, dst, code);
	}

	/**
	 * Extracts given channel from an image.
	 * @param src the image to extract.
	 * @param channel zero indexed channel number to extract.
	 * @param dst output image.
	 */
	private void cvExtractchannel(Mat src, double channel, Mat dst) {
		Core.extractChannel(src, dst, (int)channel);
	}

	/**
	 * An indication of which type of filter to use for a blur.
	 * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
	 */
	enum BlurType{
		BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
			BILATERAL("Bilateral Filter");

		private final String label;

		BlurType(String label) {
			this.label = label;
		}

		public static BlurType get(String type) {
			if (BILATERAL.label.equals(type)) {
				return BILATERAL;
			}
			else if (GAUSSIAN.label.equals(type)) {
			return GAUSSIAN;
			}
			else if (MEDIAN.label.equals(type)) {
				return MEDIAN;
			}
			else {
				return BOX;
			}
		}

		@Override
		public String toString() {
			return this.label;
		}
	}

	/**
	 * Softens an image using one of several filters.
	 * @param input The image on which to perform the blur.
	 * @param type The blurType to perform.
	 * @param doubleRadius The radius for the blur.
	 * @param output The image in which to store the output.
	 */
	private void blur(Mat input, BlurType type, double doubleRadius,
		Mat output) {
		int radius = (int)(doubleRadius + 0.5);
		int kernelSize;
		switch(type){
			case BOX:
				kernelSize = 2 * radius + 1;
				Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
				break;
			case GAUSSIAN:
				kernelSize = 6 * radius + 1;
				Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
				break;
			case MEDIAN:
				kernelSize = 2 * radius + 1;
				Imgproc.medianBlur(input, output, kernelSize);
				break;
			case BILATERAL:
				Imgproc.bilateralFilter(input, output, -1, radius, radius);
				break;
		}
	}

	/**
	 * Calculates the sum of two Mats.
	 * @param src1 the first Mat
	 * @param src2 the second Mat
	 * @param out the Mat that is the sum of the two Mats
	 */
	private void cvAdd(Mat src1, Mat src2, Mat out) {
		Core.add(src1, src2, out);
	}
}
