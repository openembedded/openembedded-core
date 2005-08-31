# gcc-3.4 blows up in gtktext with -frename-registers on arm-linux
python () {
	cflags = (bb.data.getVar('CFLAGS', d, 1) or '').replace('-frename-registers', '')
	bb.data.setVar('CFLAGS', cflags, d)
}
