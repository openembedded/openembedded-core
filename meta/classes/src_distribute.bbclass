SRC_DISTRIBUTECOMMAND[func] = "1"
python do_distribute_sources () {
	l = bb.data.createCopy(d)
	bb.data.update_data(l)

	sources_dir = bb.data.getVar('SRC_DISTRIBUTEDIR', d, 1)
	src_uri = bb.data.getVar('SRC_URI', d, 1).split()
	fetcher = bb.fetch2.Fetch(src_uri, d)
	ud = fetcher.ud

	licenses = bb.data.getVar('LICENSE', d, 1).replace('&', '|')
	licenses = licenses.replace('(', '').replace(')', '')
	clean_licenses = ""
	for x in licenses.split():
		if x.strip() == '' or x == 'CLOSED':
			continue

		if x != "|":
			clean_licenses += x

	for license in clean_licenses.split('|'):
		for url in ud.values():
			cmd = bb.data.getVar('SRC_DISTRIBUTECOMMAND', d, 1)
			if not cmd:
				raise bb.build.FuncFailed("Unable to distribute sources, SRC_DISTRIBUTECOMMAND not defined")
			url.setup_localpath(d)
			bb.data.setVar('SRC', url.localpath, d)
			if url.type == 'file':
				if url.basename == '*':
					import os.path
					dest_dir = os.path.basename(os.path.dirname(os.path.abspath(url.localpath)))
					bb.data.setVar('DEST', "%s_%s/" % (bb.data.getVar('PF', d, 1), dest_dir), d)
				else:
					bb.data.setVar('DEST', "%s_%s" % (bb.data.getVar('PF', d, 1), url.basename), d)
			else:
				bb.data.setVar('DEST', '', d)

			bb.data.setVar('SRC_DISTRIBUTEDIR', "%s/%s" % (sources_dir, license), d)
			bb.build.exec_func('SRC_DISTRIBUTECOMMAND', d)
}

addtask distribute_sources before do_build after do_fetch

addtask distribute_sources_all after do_distribute_sources
do_distribute_sources_all[recrdeptask] = "do_distribute_sources"
do_distribute_sources_all[nostamp] = "1"
do_distribute_sources_all () {
	:
}
