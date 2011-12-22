def prserv_get_pr_auto(d):
    import oe.prservice
    if d.getVar('USE_PR_SERV', True) != "1":
        bb.warn("Not using network based PR service")
        return None

    version = d.getVar("PRAUTOINX", True)
    pkgarch = d.getVar("PACKAGE_ARCH", True)
    checksum = d.getVar("BB_TASKHASH", True)

    if d.getVar('PRSERV_LOCKDOWN', True):
        auto_rev = d.getVar('PRAUTO_' + version + '_' + pkgarch, True) or d.getVar('PRAUTO_' + version, True) or None
    else:
        conn = d.getVar("__PRSERV_CONN", True)
        if conn is None:
            conn = oe.prservice.prserv_make_conn(d)
            if conn is None:
                return None
        auto_rev = conn.getPR(version, pkgarch, checksum)

    return auto_rev
