#!/bin/bash

# Nome do namespace
NAMESPACE="fiap-video-processor"

# Função para verificar se o namespace existe
check_namespace() {
    kubectl get namespace "$NAMESPACE" >/dev/null 2>&1
}

# Criar namespace se não existir
if ! check_namespace; then
    echo "Namespace '$NAMESPACE' não encontrado. Criando..."
    kubectl create namespace "$NAMESPACE"
else
    echo "Namespace '$NAMESPACE' já existe. Continuando..."
fi

# Aplicar os recursos na ordem correta
echo "Aplicando os recursos do Kubernetes na ordem correta..."

# ConfigMap e Secrets (configurações sensíveis e iniciais)
kubectl apply -f ../manifests/secrets.yaml
kubectl apply -f ../manifests/config-db.yaml

# Persistência (PV e PVC para o banco de dados)
kubectl apply -f ../manifests/pv-db.yaml
kubectl apply -f ../manifests/pvc-db.yaml

# Banco de Dados (Deployment e Service do MongoDB)
kubectl apply -f ../manifests/deployment-db.yaml
kubectl apply -f ../manifests/service-db.yaml

# Aplicação (Deployment e Service da aplicação)
kubectl apply -f ../manifests/deployment-app.yaml
kubectl apply -f ../manifests/service-app.yaml

# Autoescalonamento (HPA da aplicação)
kubectl apply -f ../manifests/hpa-app.yaml

# Fim do script
echo "Implantação concluída."
