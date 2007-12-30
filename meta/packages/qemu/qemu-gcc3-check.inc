python __anonymous() {
    from bb import which, data

    path = data.getVar('PATH', d, 1)
    oldOeConf = data.getVar('EXTRA_OECONF', d, 1)
    if not oldOeConf: oldOeConf = ""
    if len(which(path, 'gcc-3.4.4')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc-3.4.4", d)
    elif len(which(path, 'gcc-3.4')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc-3.4", d)
    elif len(which(path, 'gcc34')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc34", d)
    elif len(which(path, 'gcc33')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc33", d)
    elif len(which(path, 'gcc-3.3')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc-3.3", d)
    elif len(which(path, 'gcc-3.3.6')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc-3.3.6", d)
    elif len(which(path, 'gcc-3.4.6')) != 0:
        data.setVar('EXTRA_OECONF', oldOeConf + " --cc=gcc-3.4.6", d)
}

