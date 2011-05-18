def prserv_make_conn(d):
    import prserv.serv
    host=d.getVar("PRSERV_HOST",True)
    port=d.getVar("PRSERV_PORT",True)
    try:
        conn=None
        conn=prserv.serv.PRServerConnection(host,int(port))
        d.setVar("__PRSERV_CONN",conn)
    except Exception, exc:
        bb.fatal("Connecting to PR service %s:%s failed: %s" % (host, port, str(exc)))

    return conn

def prserv_get_pr_auto(d):
    if not d.getVar('USE_PR_SERV', True):
        bb.warn("Not using network based PR service")
        return None

    conn=d.getVar("__PRSERV_CONN", True)
    if conn is None:
        conn=prserv_make_conn(d)
        if conn is None:
            return None

    version=d.getVar("PF", True)
    checksum=d.getVar("BB_TASKHASH", True)
    auto_rev=conn.getPR(version,checksum)
    bb.debug(1,"prserv_get_pr_auto: version: %s checksum: %s result %d" % (version, checksum, auto_rev))
    return auto_rev
