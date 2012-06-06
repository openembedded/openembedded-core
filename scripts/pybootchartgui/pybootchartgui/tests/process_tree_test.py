import sys
import os
import unittest

sys.path.insert(0, os.getcwd())

import parsing
import process_tree

class TestProcessTree(unittest.TestCase):

    def setUp(self):
	self.name = "Process tree unittest"
        self.rootdir = '../examples/1'
	self.ps_stats = parsing.parse_file(parsing.ParserState(), self.mk_fname('proc_ps.log')).ps_stats
        self.processtree = process_tree.ProcessTree(self.ps_stats, None, False, for_testing = True)

    def mk_fname(self,f):
        return os.path.join(self.rootdir, f)

    def flatten(self, process_tree):
        flattened = []
        for p in process_tree:
            flattened.append(p)
            flattened.extend(self.flatten(p.child_list))
        return flattened

    def checkAgainstJavaExtract(self, filename, process_tree):
        for expected, actual in zip(open(filename), self.flatten(process_tree)):
            tokens = expected.split('\t')
            self.assertEqual(int(tokens[0]), actual.pid)
            self.assertEqual(tokens[1], actual.cmd)
            self.assertEqual(long(tokens[2]), 10 * actual.start_time)
            self.assert_(long(tokens[3]) - 10 * actual.duration < 5, "duration")
            self.assertEqual(int(tokens[4]), len(actual.child_list))
            self.assertEqual(int(tokens[5]), len(actual.samples))

    def testBuild(self):
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.1.log'), process_tree)

    def testMergeLogger(self):
        self.processtree.merge_logger(self.processtree.process_tree, 'bootchartd', None, False)
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.2.log'), process_tree)

    def testPrune(self):
        self.processtree.merge_logger(self.processtree.process_tree, 'bootchartd', None, False)
        self.processtree.prune(self.processtree.process_tree, None)
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.3b.log'), process_tree)

    def testMergeExploders(self):
        self.processtree.merge_logger(self.processtree.process_tree, 'bootchartd', None, False)
        self.processtree.prune(self.processtree.process_tree, None)
        self.processtree.merge_exploders(self.processtree.process_tree, set(['hwup']))
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.3c.log'), process_tree)

    def testMergeSiblings(self):
        self.processtree.merge_logger(self.processtree.process_tree, 'bootchartd', None, False)
        self.processtree.prune(self.processtree.process_tree, None)
        self.processtree.merge_exploders(self.processtree.process_tree, set(['hwup']))
        self.processtree.merge_siblings(self.processtree.process_tree)
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.3d.log'), process_tree)

    def testMergeRuns(self):
        self.processtree.merge_logger(self.processtree.process_tree, 'bootchartd', None, False)
        self.processtree.prune(self.processtree.process_tree, None)
        self.processtree.merge_exploders(self.processtree.process_tree, set(['hwup']))
        self.processtree.merge_siblings(self.processtree.process_tree)
        self.processtree.merge_runs(self.processtree.process_tree)
        process_tree = self.processtree.process_tree
        self.checkAgainstJavaExtract(self.mk_fname('extract.processtree.3e.log'), process_tree)

if __name__ == '__main__':
    unittest.main()
