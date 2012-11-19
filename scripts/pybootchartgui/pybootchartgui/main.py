import sys
import os
import optparse

import parsing
import gui
import batch

def _mk_options_parser():
	"""Make an options parser."""
	usage = "%prog [options] /path/to/tmp/buildstats/<recipe-machine>/<BUILDNAME>/"
	version = "%prog v1.0.0"
	parser = optparse.OptionParser(usage, version=version)
	parser.add_option("-i", "--interactive", action="store_true", dest="interactive", default=False, 
			  help="start in active mode")
	parser.add_option("-f", "--format", dest="format", default="svg", choices=["svg", "pdf", "png"],
			  help="image format: svg, pdf, png, [default: %default]")
	parser.add_option("-o", "--output", dest="output", metavar="PATH", default=None,
			  help="output path (file or directory) where charts are stored")
	parser.add_option("-s", "--split", dest="num", type=int, default=1,
			  help="split the output chart into <NUM> charts, only works with \"-o PATH\"")
	parser.add_option("-m", "--mintime", dest="mintime", type=int, default=8,
			  help="only tasks longer than this time will be displayed")
	parser.add_option("-n", "--no-prune", action="store_false", dest="prune", default=True,
			  help="do not prune the process tree")
	parser.add_option("-q", "--quiet", action="store_true", dest="quiet", default=False,
			  help="suppress informational messages")
	parser.add_option("--very-quiet", action="store_true", dest="veryquiet", default=False,
			  help="suppress all messages except errors")
	parser.add_option("--verbose", action="store_true", dest="verbose", default=False,
			  help="print all messages")
	return parser

def _get_filename(path):
	"""Construct a usable filename for outputs"""
	dir = "."
	file = "bootchart"
	if os.path.isdir(path):
		dir = path
	elif path != None:
		file = path
	return os.path.join(dir, file)

def main(argv=None):
	try:
		if argv is None:
			argv = sys.argv[1:]
	
		parser = _mk_options_parser()
		options, args = parser.parse_args(argv)
	
		if len(args) == 0:
			parser.error("insufficient arguments, expected at least one path.")
			return 2

		res = parsing.parse(args, options.prune, options.mintime)
		if options.interactive or options.output == None:
			gui.show(res)
		else:
			filename = _get_filename(options.output)
			res_list = parsing.split_res(res, options.num)
			n = 1
			for r in res_list:
				if len(res_list) == 1:
					f = filename + "." + options.format
				else:
					f = filename + "_" + str(n) + "." + options.format
					n = n + 1
				batch.render(r, options.format, f)
				print "bootchart written to", f
		return 0
	except parsing.ParseError, ex:
		print("Parse error: %s" % ex)
		return 2


if __name__ == '__main__':
	sys.exit(main())
