/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.tree;

import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * Backer bean for use in example.  Basically makes a TreeNode available.
 * 
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class TreeBacker 
{
    public TreeNode treeData = new TreeNodeBase("foo-folder", "Inbox", true);

    public TreeBacker() 
    {
        // construct a set of fake data (normally your data would come from a database)

        // populate Frank's portion of the tree
        TreeNodeBase personNode = new TreeNodeBase("person", "Frank Foo", true);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", true));        
        TreeNodeBase folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050001", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", false));
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Recommendation", true));        
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Approval", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050001", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050003", false));        
        folderNode.getChildren().add(new TreeNodeBase("document", "E050011", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "R050002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "C050003", false));                
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "X050003", false));        
        folderNode.getChildren().add(new TreeNodeBase("document", "X050011", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "F050002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "G050003", false));                
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "J050006", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J050007", false));
        personNode.getChildren().add(folderNode);

        treeData.getChildren().add(personNode);

        // populate Betty's portion of the tree
        personNode = new TreeNodeBase("person", "Betty Bar", true);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo", true));        
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Reviewer", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "X012000", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "X013000", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "X014000", false));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("foo-folder", "Requires Foo Recommendation", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010026", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J020002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030103", false));        
        folderNode.getChildren().add(new TreeNodeBase("document", "E030214", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "R020444", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "C010000", false));                
        personNode.getChildren().add(folderNode);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Requires Foo Approval", true));        
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Processing", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "T052003", false));        
        folderNode.getChildren().add(new TreeNodeBase("document", "T020011", false));
        personNode.getChildren().add(folderNode);
        folderNode = new TreeNodeBase("bar-folder", "Requires Bar Approval", true);
        folderNode.getChildren().add(new TreeNodeBase("document", "J010002", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "J030047", false));
        folderNode.getChildren().add(new TreeNodeBase("document", "F030112", false));
        personNode.getChildren().add(folderNode);        

        treeData.getChildren().add(personNode);
    }

    public TreeNode getTreeData()
    {
        return treeData;
    }
}





