python __anonymous() {
    from bb import which, data

    path = data.getVar('PATH', d, 1)
    oldOeConf = data.getVar('EXTRA_OECONF', d, 1)
    if not oldOeConf: oldOeConf = ""
    gcc3 = check_gcc3(d)
    if gcc3:
	data.setVar('EXTRA_OECONF', oldOeConf + " --cc=" + gcc3, d)
}

