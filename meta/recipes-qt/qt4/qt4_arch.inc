inherit siteinfo

ARM_INSTRUCTION_SET = "arm"

def qt_arch(d):
    import bb, re
    arch = d.getVar('TARGET_ARCH', True)
    if re.match("^i.86$", arch):
        arch = "i386"
    elif re.match("^arm.*", arch):
        arch = "arm"
    elif arch == "x86_64":
        arch = "x86"
    elif arch == "mipsel":
        arch = "mips"
    return arch

def qt_endian(d):
    import bb
    if d.getVar('SITEINFO_ENDIANNESS', True) == "le":
        return "-little-endian"
    elif d.getVar('SITEINFO_ENDIANNESS', True) == "be":
        return "-big-endian"
    else:
        assert False
